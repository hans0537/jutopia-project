import android.app.Application
import android.content.Intent
import android.icu.text.NumberFormat
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.myapplication.common.R
import java.text.DecimalFormat
import java.util.Locale

actual fun getPlatformName(): String = "Android"

@Composable fun MainView() = App()

actual val icehimchanFontFamily: FontFamily = FontFamily(
    Font(R.font.icehimchan)
)
actual val icejaramFontFamily: FontFamily = FontFamily(
    Font(R.font.icejaram)
)
actual val icesiminFontFamily: FontFamily = FontFamily(
    Font(R.font.icesimin)
)
actual val icesotongFontFamily: FontFamily = FontFamily(
    Font(R.font.icesotong)
)
actual val pretendardFontFamily: FontFamily = FontFamily(
    Font(R.font.pretendard)
)

actual fun formatDouble(value: Double, decimalPlaces: Int): String {
    val formatString = StringBuilder("#.")
    repeat(decimalPlaces) {
        formatString.append("#")
    }
    val formatter = DecimalFormat(formatString.toString())
    return formatter.format(value)
}

//class AndroidApp : Application() {
//    companion object {
//        lateinit var INSTANCE: AndroidApp
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        INSTANCE = this
//    }
//}

actual fun addComma(value: Double): String {
    val formatter = NumberFormat.getInstance(Locale.KOREA)
    return formatter.format(value)
}

actual fun openUrl(url: String?) {
    val uri = url?.let { Uri.parse(it) } ?: return

    val intent = Intent().apply {
        action = Intent.ACTION_VIEW
        data = uri
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

//    AndroidApp.INSTANCE.startActivity(intent)
}
