package com.bluixe.jani

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.*


class Diary : AppCompatActivity() {
    private val pages: MutableList<View> = ArrayList()
    lateinit var imageView: ImageView
    lateinit var editText: EditText
    var img_uri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)
        imageView = findViewById(R.id.image_view)
        editText = findViewById(R.id.edit_text)
//        val adapter = ViewAdapter()
        val choose_btn = findViewById<Button>(R.id.choose_image)
        val gene_btn = findViewById<Button>(R.id.gene_image)
        choose_btn.setOnClickListener {

            val checkWrite= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            val checkRead= ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);
            val ok= PackageManager.PERMISSION_GRANTED;

            if (checkWrite!= ok && checkRead!=ok){
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),1);
            }else {
                var intent= Intent("android.intent.action.GET_CONTENT");
                //把所有照片显示出来
                intent.setType("image/*");
                startActivityForResult(intent,123);
            }
        }
        gene_btn.setOnClickListener {
            val view = layoutInflater.inflate(R.layout.gene_diary,null)
            val mtv = view.findViewById<ImageView>(R.id.image)
            mtv.setImageURI(img_uri)
            val tx = view.findViewById<TextView>(R.id.text)
            tx.text = editText.text
//            view.measure(100, 200)
            val bitmap = createBitmap(view, 600, 1200)
//            saveBitmapToSdCard(this,bitmap!!,"0514xx")
            saveImage(bitmap!!, this, "0514")
        }

    }

    fun createBitmap(v: View, width: Int, height: Int): Bitmap? {
        //测量使得view指定大小
        val measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
        val measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        v.measure(measuredWidth, measuredHeight)
        //调用layout方法布局后，可以得到view的尺寸大小
        v.layout(0, 0, v.measuredWidth, v.measuredHeight)
        val bmp = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        c.drawColor(Color.WHITE)
        v.draw(c)
        return bmp
    }

    private fun saveImage(bitmap: Bitmap, context: Context, folderName: String) {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            val values = contentValues()
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + folderName)
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            // RELATIVE_PATH and IS_PENDING are introduced in API 29.

            val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
            }
        } else {
            val directory = File(Environment.getExternalStorageDirectory().toString() + '/' + folderName)
            // getExternalStorageDirectory is deprecated in API 29

            if (!directory.exists()) {
                directory.mkdirs()
            }
            val fileName = System.currentTimeMillis().toString() + ".png"
            val file = File(directory, fileName)
            saveImageToStream(bitmap, FileOutputStream(file))
            if (file.absolutePath != null) {
                val values = contentValues()
                values.put(MediaStore.Images.Media.DATA, file.absolutePath)
                // .DATA is deprecated in API 29
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            }
        }
    }

    private fun contentValues() : ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return values
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveBitmapToSdCard(context: Context, mybitmap: Bitmap, name: String): Boolean {
        var result = false
        //创建位图保存目录
        val path = Environment.getExternalStorageDirectory().toString() + "/hhh/"
        val sd = File(path)
        if (!sd.exists()) {
            sd.mkdir()
        }
//        Toast.makeText(context, "this", Toast.LENGTH_SHORT).show()
        val file = File("$path$name.jpg")
        var fileOutputStream: FileOutputStream? = null
        if (!file.exists()) {
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                    fileOutputStream = FileOutputStream(file)
                    mybitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
                    fileOutputStream.flush()
                    fileOutputStream.close()

                    //update gallery
                    val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
                    val uri = Uri.fromFile(file)
                    intent.data = uri
                    context.sendBroadcast(intent)
                    Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show()
                    result = true
                } else {
                    Toast.makeText(context, "不能读取到SD卡", Toast.LENGTH_SHORT).show()
                }
            } catch (e: FileNotFoundException) {
                Toast.makeText(context, "FileNotFoundException", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            } catch (e: IOException) {
                Toast.makeText(context, "IOException", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
        return result
    }



    override fun onActivityResult(requestCode: Int,resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==123){
            val uri: Uri?= data!!.data;
            if (DocumentsContract.isDocumentUri(this,uri)){
                val docId=DocumentsContract.getDocumentId(uri);//docId=image:26
                val uri_getAuthority=uri!!.authority;
                if ("com.android.providers.media.documents".equals(uri_getAuthority)){
                    val id=docId.split(":")[1];//id="26"
                    val baseUri=Uri.parse("content://media/external/images/media");
                    imageView.setImageURI(Uri.withAppendedPath(baseUri, id));
                    img_uri = Uri.withAppendedPath(baseUri, id)
                }
            }

        }
    }
}