package com.dicoding.mentoring.ui.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.dicoding.mentoring.R
import com.dicoding.mentoring.databinding.FragmentProfileBinding
import java.io.File

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var currPhotoPath: String
    private lateinit var getFile: File
    private val PICK_IMAGE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        binding.chipChangePicture.setOnClickListener {
            openGallery()
        }

        //action when click Add Interest
        binding.chipAddInterest.setOnClickListener{
            val intent = Intent(activity, ListInterestActivity::class.java)
            activity?.startActivity(intent)
        }

        //action when click button save
        binding.btnSave.setOnClickListener{
            Toast.makeText(context,"Data berhasil disimpan",Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment_profile,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.action_logout ->{
                Toast.makeText(context,"Action logo berhasil",Toast.LENGTH_SHORT).show()
            }
        }
        return super.onContextItemSelected(item)
    }

//    private fun getEdTextData(){
//        val getEmail = binding.editTextEmail.text.toString()
//        val getUsername = binding.editTextUsername.text.toString()
//        val getFullname = binding.editTextFullname.text.toString()
//    }


    private fun openGallery(){
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent,PICK_IMAGE_REQUEST)
    }

    @Suppress
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
            binding.imgProfilePic.setImageURI(imageUri)
            // Save the imageUri for further use, such as updating user profile
        }
    }



}