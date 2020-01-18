package io.grx.tianji;

import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

/**
 * PlainSocketFactory工厂类
 * @author 融360-TJ
 *
 */
public class MyPlainSocketFactory extends PlainConnectionSocketFactory {

	/**
	 * 创建socket
	 * @param context Http上下文
	 */
	public Socket createSocket(final HttpContext context) throws IOException {
		InetSocketAddress socksAddr = (InetSocketAddress) context
				.getAttribute("socks.address");
		if (socksAddr != null) {
			Proxy proxy = new Proxy(Proxy.Type.SOCKS, socksAddr);
			return new Socket(proxy);
		}
		return new Socket();
	}
}
