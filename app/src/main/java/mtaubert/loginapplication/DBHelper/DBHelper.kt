package mtaubert.loginapplication.DBHelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import mtaubert.loginapplication.Model.User

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {
    companion object {
        private val DATABASE_NAME = "LoginAppDB"
        private val DATABASE_VER = 1

        //Table
        private val TABLE_NAME = "User"
        private val COL_NAME = "Name"
        private val COL_EMAIL = "Email"
        private val COL_PASSWORD = "Password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY =
            ("CREATE TABLE $TABLE_NAME ($COL_EMAIL TEXT PRIMARY KEY, $COL_NAME TEXT, $COL_PASSWORD TEXT)")
        db!!.execSQL(CREATE_TABLE_QUERY)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    val allUser: List<User>
        get() {
            var listUser = ArrayList<User>()
            val selectQuery = "SELECT * FROM $TABLE_NAME"
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val user = User()
                    user.name = cursor.getString(cursor.getColumnIndex(COL_NAME))
                    user.email = cursor.getString(cursor.getColumnIndex(COL_EMAIL))
                    user.password = cursor.getString(cursor.getColumnIndex(COL_PASSWORD))

                    listUser.add(user)
                } while (cursor.moveToNext())
            }
            db.close()
            return listUser
        }

    fun addPerson(user: User) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COL_NAME, user.name)
        values.put(COL_EMAIL, user.email)
        values.put(COL_PASSWORD, user.password)

        db.insert(TABLE_NAME, null, values)
    }

    fun doesUserExist(user: User):Boolean {
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COL_EMAIL='${user.email}'"
        val cursor = db.rawQuery(selectQuery, null)
        if(cursor.columnCount > 0) {
            return true
        }
        cursor.close()
        return false
    }

    fun validateLogin(email: String, password: String): User? {
        var user: User? = null
        val db = this.readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COL_EMAIL='$email'"
        val cursor = db.rawQuery(selectQuery, null)
        if(cursor.moveToFirst()) {
            user = User()
            user.name = cursor.getString(cursor.getColumnIndex(COL_NAME))
            user.email = cursor.getString(cursor.getColumnIndex(COL_EMAIL))
            user.password = cursor.getString(cursor.getColumnIndex(COL_PASSWORD))

        }
        cursor.close()

        if(user == null) {
            return null
        } else {
            if(user.password == password) {
                return user
            } else {
                return null
            }
        }
    }
}