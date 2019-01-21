package aufgabe7;

import java.util.Properties;
import java.util.Scanner;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class AbstimmClient {

	private QueueSession abstimmSession;
	private QueueSender abstimmSender;
	private TopicSession umfrageSession;
	private TopicSubscriber umfrageSubscriber;

	public AbstimmClient() throws NamingException, JMSException {
		super();

		this.connectQueuesAndTopics();

		this.startUmfrageListener();

		this.abstimmen();
	}

	private void abstimmen() {
		Scanner scanner = new Scanner(System.in);

		boolean validInput = false;
		String input = null;

		while (!validInput) {
			synchronized (System.out) {
				System.out.println("Wie möchten Sie abstimmen?");
			}

			input = scanner.nextLine().toLowerCase();
			if (input.equals("ja") || input.equals("nein") || input.equals("enthaltung")) {
				validInput = true;
			}
		}

		try {
			
			synchronized(System.out) {
				System.out.println("Creating abstimm Message...");
				Message abstimmMessage = abstimmSession.createMessage();
				abstimmMessage.setStringProperty("Stimme", input);
				
				System.out.println("Message created! Sending...");
				abstimmSender.send(abstimmMessage);
				System.out.println("Message sent!\r\n");
			}
			
		} catch (JMSException e) {
			e.printStackTrace();
		}

		scanner.close();
	}

	private void startUmfrageListener() {
		new Thread(() -> {

			while (true) {
				try {
					Message umfrageMessage = umfrageSubscriber.receive();
					String umfrage = umfrageMessage.getStringProperty("umfrage");

					synchronized (System.out) {
						System.out.println("------------- neue Umfrage --------------");
						System.out.println(umfrage);
						System.out.println("-----------------------------------------");
					}

					Thread.sleep(50);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}).start();
	}

	private void connectQueuesAndTopics() throws NamingException, JMSException {
		Properties jndiProperties = new Properties();
		jndiProperties.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		jndiProperties.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
		jndiProperties.put("java.naming.provider.url", Data.SERVER_URL);

		Context context = new InitialContext(jndiProperties);
		QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) context.lookup("ConnectionFactory");
		QueueConnection queueConnection = queueConnectionFactory.createQueueConnection(Data.USER, Data.PASSWORD);
		abstimmSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue abstimmQueue = (Queue) context.lookup(Data.QUEUE_ABSTIMMUNG);

		abstimmSender = abstimmSession.createSender(abstimmQueue);

		TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) context.lookup("ConnectionFactory");
		TopicConnection topicConnection = topicConnectionFactory.createTopicConnection(Data.USER, Data.PASSWORD);
		umfrageSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
		Topic umfrageTopic = (Topic) context.lookup(Data.TOPIC_UMFRAGE);

		umfrageSubscriber = umfrageSession.createSubscriber(umfrageTopic);
		
		queueConnection.start();
        topicConnection.start();
	}

	public static void main(String[] args) throws NamingException, JMSException {
		AbstimmClient client = new AbstimmClient();
	}
}
