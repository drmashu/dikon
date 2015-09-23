import io.github.drmashu.buri.HtmlAction
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *
 */
public class SampleAction(request: HttpServletRequest, response: HttpServletResponse): HtmlAction(request, response) {
    public override fun get() {

    }
}