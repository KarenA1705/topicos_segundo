package com.example.myapplication.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myapplication.model.Character

class DataBase(context: Context) : SQLiteOpenHelper(context, "CHARACTERS.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = (""" CREATE TABLE IF NOT EXISTS CHARACTERS_DB ( ID INTEGER PRIMARY KEY AUTOINCREMENT, 
            NAME TEXT NOT NULL,
            DESCRIPTION TEXT,
            MAIN_CHARACTER INTEGER,
            SECONDARY_CHARACTER INTEGER,
            EXTRA_CHARACTER INTEGER,
            IMAGE TEXT ) """)
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        /**None**/
    }

    fun insertCharacter(character: Character): String {
        val db = this.writableDatabase
        val contentValues = ContentValues().apply {
            put("NAME", character.nameCharacter)
            put("DESCRIPTION", character.description)
            put("MAIN_CHARACTER", character.mainCharacter)
            put("SECONDARY_CHARACTER", character.secondaryCharacter)
            put("EXTRA_CHARACTER", character.extraCharacter)
            put("IMAGE", character.image)
        }

        val resultDB = db.insert("CHARACTERS_DB", null, contentValues)
        db.close()
        return if (resultDB == (-1).toLong()) "Ocurrió un error. Inténtelo más tarde" else "Personaje agregado correctamente"
    }

    fun selectAllCharacter(): MutableList<Character> {
        val characterList = mutableListOf<Character>()
        val dataBase = this.readableDatabase
        val cursor = dataBase.rawQuery("SELECT * FROM CHARACTERS_DB", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"))
                val characterName = cursor.getString(cursor.getColumnIndexOrThrow("NAME"))
                val description = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION"))
                val mainCharacter = cursor.getInt(cursor.getColumnIndexOrThrow("MAIN_CHARACTER"))
                val secondaryCharacter = cursor.getInt(cursor.getColumnIndexOrThrow("SECONDARY_CHARACTER"))
                val extraCharacter = cursor.getInt(cursor.getColumnIndexOrThrow("EXTRA_CHARACTER"))
                val image = cursor.getString(cursor.getColumnIndexOrThrow("IMAGE"))
                val character = Character(
                    id,
                    characterName,
                    description,
                    mainCharacter,
                    secondaryCharacter,
                    extraCharacter,
                    image
                )
                characterList.add(character)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return characterList
    }
}