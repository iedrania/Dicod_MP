package com.dicoding.mentoring.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.mentoring.R
import com.dicoding.mentoring.data.local.PostUserProfileResponse
import com.dicoding.mentoring.data.remote.network.ApiConfig
import com.dicoding.mentoring.databinding.ActivityEditProfileBinding
import com.dicoding.mentoring.ui.onboard.OnboardActivity
import com.dicoding.mentoring.utils.convUriToFile
import com.dicoding.mentoring.utils.reduceFileImage
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditProfileActivity : AppCompatActivity() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: ActivityEditProfileBinding

    //set getFile into null to prevent error when update profile
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Profil")
        
        checkCurrentUser()

        //validate the save button
        attachPhoneValidation(binding.editTextPhone)
        attachNameValidation(binding.editTextFullname)
        attachGenderValidation(binding.radioMale,binding.radioFemale)
        setSaveButtonEnable()
        binding.editTextFullname.doOnTextChanged { _, _, _, _ ->
            setSaveButtonEnable()
        }

        binding.editTextPhone.doOnTextChanged { _, _, _, _ ->
            setSaveButtonEnable()
        }

        binding.radioMale.setOnCheckedChangeListener { _, _ ->
            setSaveButtonEnable()
        }

        binding.radioFemale.setOnCheckedChangeListener { _, _ ->
            setSaveButtonEnable()
        }

        //Obtain ViewModel
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        profileViewModel.isLoading.observe(this) { showLoading(it) }
        profileViewModel.isError.observe(this) { showError(it) }
        profileViewModel.isSuccess.observe(this) {
            finish()
        }

        binding.btnChangePicture.setOnClickListener { galleryAction() }

        //Update profile when save button clicked
        binding.btnSave.setOnClickListener {
            val user = Firebase.auth.currentUser

            user.let {
                user?.getIdToken(false)?.addOnSuccessListener {
                    if (getFile != null) uploadImage(it.token)
                    updateUserDataProfile(it.token)
                }
            }
        }
    }

    private fun checkCurrentUser() {
        val user = Firebase.auth.currentUser
        if (user != null) {
            renderProfilePage(user)
        } else {
            val intent = Intent(this@EditProfileActivity, OnboardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun renderProfilePage(user: FirebaseUser) {
        user.let {
            user.getIdToken(false).addOnSuccessListener {
                getUserDataProfile(it.token)
            }
        }
    }

    private fun getUserDataProfile(token: String?) {
        Log.d(
            "ProfileFragment",
            "getUserDataProfile : token user pada profile fragment adalah : $token"
        )
        profileViewModel.getProfile(token)
        profileViewModel.userProfile.observe(this) {
            if (it.profile_picture_url.isNullOrEmpty()) {
                Glide.with(this)
                    .load("https://static.vecteezy.com/system/resources/thumbnails/003/337/584/small/default-avatar-photo-placeholder-profile-icon-vector.jpg")
                    .into(binding.imgProfilePic)
            } else {
                Glide.with(this)
                    .load(it.profile_picture_url)
                    .into(binding.imgProfilePic)
            }
            binding.editTextFullname.setText(it.name)
            binding.editTextPhone.setText(it.phone)
            binding.editTextEmail.setText(it.email)
            binding.editTextBiography.setText(it.bio)
            if (it.is_mentor) {
                binding.editTextRole.setText("Mentor")
            } else {
                binding.editTextRole.setText("Mentee")
            }
            if (it.gender_id == 1) {
                binding.radioMale.isChecked = true
            } else if (it.gender_id == 2) {
                binding.radioFemale.isChecked = true
            }
        }
    }

    private fun updateUserDataProfile(token: String?) {
        var gender_id: Int? = null
        profileViewModel.userProfile.observe(this) {

            if (binding.radioMale.isChecked) {
                it.gender_id = 1
            } else if (binding.radioFemale.isChecked) {
                it.gender_id = 2
            }
            gender_id = it.gender_id
        }
        profileViewModel.updateProfile(
            token,
            binding.editTextFullname.text.toString(),
            gender_id,
            binding.editTextPhone.text.toString(),
            binding.editTextBiography.text.toString(),
            binding.editTextEmail.text.toString()
        )
        println(gender_id)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(isError: Boolean) {
        if (isError) {
            Toast.makeText(
                this, getString(R.string.profile_update_failed), Toast.LENGTH_SHORT
            ).show()
        }
    }

    //    Fungsi membuka gallery
    private fun galleryAction() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val selectedImage: Uri = it.data?.data as Uri
            val myFile = convUriToFile(selectedImage, this@EditProfileActivity)
            getFile = myFile
            binding.imgProfilePic.setImageURI(selectedImage)
        }
    }

    private fun uploadImage(token: String?) {
        if (getFile != null) {
            val file = reduceFileImage(getFile!!)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file",
                file.name,
                requestImageFile
            )

            val client =
                ApiConfig.getApiService().updateUserProfilePicture("Bearer $token", imageMultipart)
            client.enqueue(object : Callback<PostUserProfileResponse> {
                override fun onResponse(
                    call: Call<PostUserProfileResponse>,
                    response: Response<PostUserProfileResponse>
                ) {
                    Log.d("uploadImage", "response adalah : $response")
                    if (response.isSuccessful) {
                        Log.d("uploadImage", "Rersponse sukses, response body: ${response.body()}")
                    } else {
                        Log.e(
                            "uploadImage",
                            "Ada response namun tidak sukses. response body :  ${response.body()}"
                        )
                    }
                }

                override fun onFailure(call: Call<PostUserProfileResponse>, t: Throwable) {
                    Log.d("uploadImage", t.message.toString())
                }

            })

        }
    }

    private fun setSaveButtonEnable() {
        val nameResult = binding.editTextFullname.text
        val phoneResult = binding.editTextPhone.text.toString().trim()
        val isPhoneValid = phoneResult.matches(Regex("^(\\+62)\\d*$")) && phoneResult.length >= 10
        val isGenderSelected = binding.radioMale.isChecked || binding.radioFemale.isChecked
        binding.btnSave.isEnabled =
            nameResult != null && nameResult.toString().isNotBlank() && isPhoneValid && isGenderSelected
    }

    private fun attachPhoneValidation(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                val isValid = input.matches(Regex("^(\\+62)\\d*$")) && input.length >= 10
                editText.error =
                    if (isValid) null else "Input salah. Angka harus diawali dengan +62 dan setelahnya diikuti minimal 10 angka"
            }

        })
    }

    private fun attachNameValidation(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString().trim()
                if (text.isEmpty()) {
                    editText.error = "Nama tidak boleh kosong!"
                }
            }
        })
    }

    private fun attachGenderValidation(radioButtonMale: RadioButton, radioButtonFemale: RadioButton) {
        radioButtonMale.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked && !radioButtonFemale.isChecked) {
                // Display error if no gender is selected
                radioButtonMale.error = "Salah satu gender harus diisi"
            } else {
                // Clear error if a gender is selected
                radioButtonMale.error = null
            }
        }

        radioButtonFemale.setOnCheckedChangeListener { _, isChecked ->
            if (!isChecked && !radioButtonMale.isChecked) {
                // Display error if no gender is selected
                radioButtonFemale.error = "Salah satu gender harus diisi"
            } else {
                // Clear error if a gender is selected
                radioButtonFemale.error = null
            }
        }
    }
}