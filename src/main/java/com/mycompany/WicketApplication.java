package com.mycompany;

import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.ws.WebSocketSettings;
import org.apache.wicket.protocol.ws.api.WebSocketPushBroadcaster;
import org.apache.wicket.protocol.ws.api.message.ConnectedMessage;
import org.apache.wicket.protocol.ws.api.message.IWebSocketPushMessage;
import org.apache.wicket.serialize.java.JavaSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class WicketApplication extends WebApplication
{
	static Logger log = LoggerFactory.getLogger(WicketApplication.class);

	private static String wicketApplicationKey = null;


	public static WicketApplication getApplication() {
		//return (WicketApplication)Application.get();
		return (WicketApplication)Application.get(wicketApplicationKey);
	}

	public static void broadcast(ConnectedMessage connectedMsg, IWebSocketPushMessage message) {
		WicketApplication application = getApplication();

		WebSocketSettings webSocketSettings = WebSocketSettings.Holder.get(application);
		WebSocketPushBroadcaster broadcaster = new WebSocketPushBroadcaster(webSocketSettings.getConnectionRegistry());
		broadcaster.broadcast(connectedMsg, message);
	}





	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return StartWebSocketPage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();


		wicketApplicationKey = getApplicationKey();

		mountPage("boring", BoringStartPage.class);
		mountPage("start", StartWebSocketPage.class);
		mountPage("background", BackgroundWorkPage.class);


		// add your configuration here

		getFrameworkSettings().setSerializer(new JavaSerializer(getApplicationKey()){
			public byte[] serialize(Object obj) {
				if (obj instanceof Page) {
					Page p = (Page) obj;
					log.info("===serialize " + abreviateClassName(p) + " " + p.getId());
				}
				return super.serialize(obj);
			}

			public Object deserialize(byte[] data) {
				Object res = super.deserialize(data);
				if (res instanceof Page) {
					Page p = (Page) res;
					log.info("---deserialize " + abreviateClassName(p) + " " + p.getId());
				}
				return res;
			}
		});

	}




	public static String abreviateClassName(Object obj) {
		String fullName = obj.getClass().getName();
		int dotIdx = fullName.lastIndexOf('.');

		String res = null;
		if (dotIdx != -1) {
			res = fullName.substring(dotIdx+1);
		} else {
			res = fullName;
		}

		return res;
	}

}
