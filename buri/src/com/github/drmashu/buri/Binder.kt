package com.github.drmashu.buri

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by tnagasaw on 2015/08/13.
 */
public class Binder : HttpServlet {

    public constructor(bindMap: Map<String, String>): super() {
        bindMap
    }

    public override final fun service(req: HttpServletRequest, res: HttpServletResponse) {
        val method = req.getMethod()
        val path = req.getPathInfo()
        //val action = bindMap[Pair(method, path)]

    }
}
