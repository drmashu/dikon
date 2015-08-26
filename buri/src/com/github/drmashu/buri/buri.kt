package com.github.drmashu.buri

import com.github.drmashu.dikon.Dikon
import com.github.drmashu.dikon.Factory
import org.eclipse.jetty.http2.server.HTTP2CServerConnectionFactory
import org.eclipse.jetty.server.HttpConfiguration
import org.eclipse.jetty.server.HttpConnectionFactory
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.ServerConnector
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlet.ServletHolder

/**
 * Buri 本体.
 * @author NAGASAWA Takahiro<drmashu@gmail.com>
 * @param config 設定
 */
public class Buri(val config : Map<String, Factory<*>>) {

    /**
     * サーバ起動
     * @param portNo ポート番号
     */
    public fun start(portNo: Int) {
        val server = Server()

        var httpConf = HttpConfiguration();

        var http1 = HttpConnectionFactory(httpConf);
        var http2c = HTTP2CServerConnectionFactory(httpConf);

        val connector = ServerConnector(server)
        connector.port = portNo
        connector.addConnectionFactory(http1)
        connector.addConnectionFactory(http2c)
        server.connectors = arrayOf(connector)

        val handler = ServletContextHandler()
        handler.contextPath = "/"

        val binder = Binder(Dikon(config))
        val holder = ServletHolder(binder)
        handler.addServlet(holder, "/*")
        server.handler = handler

        server.isDumpBeforeStop = true
        server.start()
        server.join()
    }
}
