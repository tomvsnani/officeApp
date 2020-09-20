package com.example.myfirstofficeappecommerce

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html.ImageGetter
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import java.net.URLConnection


class HtmlImageGetter(var context: Context) : ImageGetter {


    @SuppressLint("UseCompatLoadingForDrawables")
   override fun getDrawable(source: String): Drawable? {
        Toast.makeText(
         context, source,
            Toast.LENGTH_LONG
        ).show()
     var a= CoroutineScope(Dispatchers.IO).async {
          var drawable: Drawable? = null
          if (source.startsWith("http")) {
              // load from internet
              val sourceURL: URL
              try {
                  sourceURL = URL(source)
                  val urlConnection: URLConnection = sourceURL.openConnection()
                  urlConnection.connect()
                  val inputStream: InputStream = urlConnection.getInputStream()
                  val bufferedInputStream = BufferedInputStream(inputStream)
                  val bm = BitmapFactory.decodeStream(bufferedInputStream)

                  // convert Bitmap to Drawable
                  drawable = BitmapDrawable(context.getResources(), bm)
                  drawable.setBounds(
                      0, 0,
                      bm.width,
                      bm.height
                  )
              } catch (e: MalformedURLException) {
                  // TODO Auto-generated catch block
                  e.printStackTrace()
              } catch (e: IOException) {
                  // TODO Auto-generated catch block
                  e.printStackTrace()
              }
          } else {
              // load from local drawable
              val dourceId: Int = context
                  .getResources()
                  .getIdentifier(source, "drawable", context.getPackageName())
              drawable = context.getResources()
                  .getDrawable(dourceId)
              drawable.setBounds(
                  0, 0, drawable.intrinsicWidth,
                  drawable.intrinsicHeight
              )
          }
          return@async drawable
      }
        return null
    }
}