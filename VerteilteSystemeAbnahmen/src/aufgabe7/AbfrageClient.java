package aufgabe7;

import java.util.Properties;
import java.util.Scanner;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class AbfrageClient {

	private QueueSession abfrageSession;
	private QueueSender abfrageSender;

	public AbfrageClient() throws NamingException, JMSException {
		super();

		this.connectQueuesAndTopics();

		this.abfragen();
	}

	private void abfragen() {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println("Enter zum Abfragen des Umfrageergebnisses, \"ende\" zum Beenden");
			String input = scanner.nextLine();
			
			if(input.equals("ende")) {
				break;
			}

			try {
				System.out.println("Create temporary reply queue...");
				Queue ergebnisQueue = abfrageSession.createTemporaryQueue();
				QueueReceiver ergebnisReceiver = abfrageSession.createReceiver(ergebnisQueue);
				System.out.println("Created!");
				
				System.out.println("Create abfrage Message...");
				Message abfrageMessage = abfrageSession.createMessage();
				abfrageMessage.setJMSReplyTo(ergebnisQueue);
				System.out.println("Created! Sending...");
				abfrageSender.send(abfrageMessage);
				System.out.println("Message sent!");
				
				
				System.out.println("Waiting for response...");
				Message ergebnisMessage = ergebnisReceiver.receive();
				System.out.println("Response received! Printing result...");
				String ergebnis = ergebnisMessage.getStringProperty("ergebnis");
				System.out.println(ergebnis);
				
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}

		scanner.close();
	}

	private void connectQueuesAndTopics() throws NamingException, JMSException {
		Properties jndiProperties = new Properties();
		jndiProperties.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		jndiProperties.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
		jndiProperties.put("java.naming.provider.url", Data.SERVER_URL);

		Context context = new InitialContext(jndiProperties);
		QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) context.lookup("ConnectionFactory");
		QueueConnection queueConnection = queueConnectionFactory.createQueueConnection(Data.USER, Data.PASSWORD);
		abfrageSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue abstimmQueue = (Queue) context.lookup(Data.QUEUE_ABFRAGE);

		abfrageSender = abfrageSession.createSender(abstimmQueue);
		
		queueConnection.start();
	}

	public static void main(String[] args) throws NamingException, JMSException {
		AbfrageClient client = new AbfrageClient();
	}
}
