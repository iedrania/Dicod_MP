package com.dicoding.mentoring.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.mentoring.data.local.PostUserProfileResponse
import com.dicoding.mentoring.data.remote.network.ApiConfig
import com.dicoding.mentoring.databinding.ActivityProfileBinding
import com.dicoding.mentoring.utils.convUriToFile
import com.dicoding.mentoring.utils.reduceFileImage
import com.google.firebase.auth.FirebaseAuth
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var binding: ActivityProfileBinding
    private lateinit var getFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Obtain Firebase user token
        val user = FirebaseAuth.getInstance().currentUser
        user?.getIdToken(false)
        // get list of all mentors for adapter
        val token = user?.getIdToken(false)?.result?.token

        //Obtain ViewModel
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        profileViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        if (token != null) {
            getUserDataProfile(token)
        }

        binding.chipChangePicture.setOnClickListener {
            galleryAction()
        }

        //Update profile when save button clicked
        binding.btnSave.setOnClickListener {
            if (token != null) {
                updateUserDataProfile(token)
                uploadImage(token)
                val resultIntent = Intent()
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
    }


    private fun getUserDataProfile(token: String) {
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
                binding.radioMentor.isChecked = true
            } else {
                binding.radioMentee.isChecked = true
            }
            if (it.gender_id == 1) {
                binding.radioMale.isChecked = true
            } else {
                binding.radioFemale.isChecked = true
            }
        }
    }


    private fun updateUserDataProfile(token: String) {
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
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
            val myFile = convUriToFile(selectedImage, this@ProfileActivity)
            getFile = myFile
            binding.imgProfilePic.setImageURI(selectedImage)
        }
    }

    private fun uploadImage(token: String) {
        if (getFile != null) {
            val file = reduceFileImage(getFile)
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file",
                file.name,
                requestImageFile
            )

            val client = ApiConfig.getApiService().updateUserProfilePicture("Bearer $token", imageMultipart)
            client.enqueue(object : Callback<PostUserProfileResponse> {
                override fun onResponse(
                    call: Call<PostUserProfileResponse>,
                    response: Response<PostUserProfileResponse>
                ) {
                    Log.d("uploadImage", "response adalah : $response")
                    if (response.isSuccessful) {
                        Log.d("uploadImage", "Rersponse sukses, response body: ${response.body()}")
                    } else {
                        Log.e("uploadImage", "Ada response namun tidak sukses. response body :  ${response.body()}")
                    }
                }

                override fun onFailure(call: Call<PostUserProfileResponse>, t: Throwable) {
                    Log.d("uploadImage",t.message.toString())
                }

            })

        }
    }


}