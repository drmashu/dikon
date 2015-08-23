package com.github.drmashu.buri

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by tnagasaw on 2015/08/13.
 */
public class Binder : HttpServlet {

    public constructor(bindMap: Map<Pair<String, String>, String>): super() {
        bindMap
    }

    public override final fun service(req: HttpServletRequest, res: HttpServletResponse) {
        val method = req.method
        val path = req.pathInfo
        //val action = bindMap[Pair(method, path)]

    }
}
