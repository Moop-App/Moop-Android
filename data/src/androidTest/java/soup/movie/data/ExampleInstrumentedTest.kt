package soup.movie.data

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @Test
    fun useAppContext() { // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals("soup.movie.data.test", appContext.packageName)
    }
}
