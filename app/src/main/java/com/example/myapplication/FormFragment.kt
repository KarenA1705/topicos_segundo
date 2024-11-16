package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.database.DataBase
import com.example.myapplication.model.Character
import kotlin.io.path.Path
import kotlin.io.path.outputStream

class FormFragment : Fragment() {
    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var mainCheckBox: CheckBox
    private lateinit var secondaryCheckBox: CheckBox
    private lateinit var extraCheckBox: CheckBox
    private lateinit var selectImage: ActivityResultLauncher<Intent>
    private lateinit var imageLoaded: ImageView
    private lateinit var buttonSave: Button
    private lateinit var dataBase: DataBase

    private var imageFileUri = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_form, container, false)
        bindViews(view)
        initListener(view)
        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun bindViews(view: View) {
        nameEditText = view.findViewById(R.id.nameCharacter)
        descriptionEditText = view.findViewById(R.id.descriptionCharacter)
        mainCheckBox = view.findViewById(R.id.characterTypeMain)
        secondaryCheckBox = view.findViewById(R.id.characterTypeSecondary)
        extraCheckBox = view.findViewById(R.id.characterTypeExtra)
        imageLoaded = view.findViewById(R.id.selectImage)
        buttonSave = view.findViewById(R.id.saveButton)
        dataBase = DataBase(requireContext())
        selectAndSaveImage()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun selectAndSaveImage() {
        selectImage =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val imageUri: Uri? = result.data?.data

                    imageUri?.let { uri ->
                        try {
                            val timestamp = System.currentTimeMillis()
                            val fileName = "image_$timestamp.jpg"
                            val filePath = Path(requireContext().filesDir.absolutePath, fileName)

                            val inputStream = requireContext().contentResolver.openInputStream(uri)

                            inputStream?.use { input ->
                                filePath.outputStream().use { output ->
                                    input.copyTo(output)
                                }
                            }

                            imageFileUri = filePath.toString()
                            imageLoaded.setImageURI(Uri.fromFile(filePath.toFile()))

                        } catch (e: Exception) {
                            Toast.makeText(
                                requireContext(),
                                "Error al seleccionar la imagen: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } ?: run {
                        Toast.makeText(
                            requireContext(),
                            "No se seleccionó ninguna imagen",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else
                    Toast.makeText(
                        requireContext(),
                        "Selección de imagen cancelada",
                        Toast.LENGTH_SHORT
                    ).show()
            }

    }

    private fun initListener(view: View) {
        imageLoaded.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            selectImage.launch(intent)
        }

        buttonSave.setOnClickListener {
            insertCharacter()
        }
    }

    private fun insertCharacter() {
        val name = nameEditText.text.toString()
        val description = descriptionEditText.text.toString()
        val mainCharacter = if (mainCheckBox.isChecked) 1 else 0
        val secondaryCharacter = if (secondaryCheckBox.isChecked) 1 else 0
        val extraCharacter = if (extraCheckBox.isChecked) 1 else 0
        val image = imageFileUri

        if (validationFields(
                name,
                description,
                image,
                mainCharacter,
                secondaryCharacter,
                extraCharacter
            )
        ) {
            dataBase.let { db ->
                val message = db.insertCharacter(
                    Character(
                        nameCharacter = name,
                        description = description,
                        mainCharacter = mainCharacter,
                        secondaryCharacter = secondaryCharacter,
                        extraCharacter = extraCharacter,
                        image = image
                    )
                )
                showMessage(message)
                clearFields()
            } ?: showMessage("Error en la base de datos")
        }
    }

    private fun validationFields(
        name: String,
        description: String,
        image: String,
        mainCharacter: Int,
        secondaryCharacter: Int,
        extraCharacter: Int
    ): Boolean {
        if (name.trim().isEmpty()) {
            showMessage(getString(R.string.msgNameWarning))
            return false
        }

        if (description.trim().isEmpty()) {
            showMessage(getString(R.string.msgDescriptionWarning))
            return false
        }

        if (image.isEmpty()) {
            showMessage(getString(R.string.msgImageWarning))
            return false
        }

        if (mainCharacter == 0 && secondaryCharacter == 0 && extraCharacter == 0) {
            showMessage(getString(R.string.msgTypeCharacterWarning))
            return false
        }
        return true
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun clearFields() {
        nameEditText.text.clear()
        descriptionEditText.text.clear()
        mainCheckBox.isChecked = false
        secondaryCheckBox.isChecked = false
        extraCheckBox.isChecked = false
        imageFileUri = ""
        imageLoaded.setImageResource(0)
        hideKeyboard()
    }

    private fun hideKeyboard() {
        nameEditText.clearFocus()
        descriptionEditText.clearFocus()
    }
}