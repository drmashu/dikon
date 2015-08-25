package com.github.drmashu.buri

import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * バインダークラス.
 * URIとViewModelを結びつけるために定義を解釈し、リクエストに応じてViewModelのアクションを呼ぶ.
 */
public class Binder(val renderPackage: String, bindMap: Map<String, String>) : HttpServlet() {
    init {
        bindMap
    }

    public override final fun service(req: HttpServletRequest, res: HttpServletResponse) {
        val method = req.getMethod()
        val path = req.getPathInfo()
        //val action = bindMap[Pair(method, path)]

    }
}
