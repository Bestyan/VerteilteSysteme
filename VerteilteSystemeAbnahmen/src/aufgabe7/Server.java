package aufgabe7;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class Server {

    private UmfrageErgebnis umfrage;

    private QueueSession queueSession;
    private QueueReceiver abstimmReceiver;
    private QueueReceiver ergebnisReceiver;

    private TopicSession umfrageSession;
    private TopicPublisher umfragePublisher;

    public Server() throws NamingException, JMSException {
        super();

        this.connectQueuesAndTopics();

        umfrage = UmfrageErgebnis.laden();
        if (umfrage == null) {
            umfrage = new UmfrageErgebnis("Meinungsumfrage: Sind Sie gut auf die Prüfungszeit vorbereitet?\r\n"
                    + "mögliche Antworten: ja|nein|enthaltung");
            UmfrageErgebnis.speichern(umfrage);
        }

        this.publishUmfrage(umfrage);

        this.watchAbstimmQueue();
        this.watchErgebnisQueue();
        
        System.out.println("Server init complete!");
    }

    private void watchErgebnisQueue() {
        new Thread(() -> {
        	System.out.println("Waiting for anfrage Messages...");

            while (true) {
                try {
                    Message message = ergebnisReceiver.receive();
                    System.out.println("Received anfrage Message!");
                    
                    Queue replyTo = (Queue) message.getJMSReplyTo();
                    QueueSender replySender = queueSession.createSender(replyTo);
                    System.out.println("Created Reply Sender!");
                    
                    Message reply = queueSession.createMessage();
                    reply.setStringProperty("ergebnis", umfrage.toString());
                    System.out.println("Created Reply Message!");
                    
                    replySender.send(reply);
                    System.out.println("Reply sent!\n");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    private void watchAbstimmQueue() {
        new Thread(() -> {
        	System.out.println("Waiting for abstimm Messages...");

            while (true) {
                try {
                    Message message = abstimmReceiver.receive();
                    System.out.println("Received abstimm Message...");
                    String votum = message.getStringProperty("Stimme");
                    if(votum == null) {
                        System.out.println(message.toString());
                    	continue;
                    }
                    votum = votum.toLowerCase();

                    if (!votum.equals("ja") && !votum.equals("nein") && !votum.equals("enthaltung")) {
                        System.out.println("Ungültige Stimme: " + votum + "\n");
                    } else {
                    	System.out.println("Stimme erhalten: " + votum + "\n");
                        umfrage.abstimmen(votum);
                        UmfrageErgebnis.speichern(umfrage);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    private void publishUmfrage(UmfrageErgebnis umfrage) throws JMSException {
        System.out.println("creating umfrage Message...");
        Message umfrageMessage = umfrageSession.createMessage();
        umfrageMessage.setStringProperty("umfrage", umfrage.getFrage());
        System.out.println("Message created! Publishing...");
        umfragePublisher.publish(umfrageMessage);
        System.out.println("Message published!\r\n");
    }

    private void connectQueuesAndTopics() throws NamingException, JMSException {
        Properties jndiProperties = new Properties();
        jndiProperties.put("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
        jndiProperties.put("java.naming.factory.url.pkgs", "org.jboss.naming:org.jnp.interfaces");
        jndiProperties.put("java.naming.provider.url", Data.SERVER_URL);

        Context context = new InitialContext(jndiProperties);
        QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) context.lookup("ConnectionFactory");
        QueueConnection queueConnection = queueConnectionFactory.createQueueConnection(Data.USER, Data.PASSWORD);
        queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
        

        Queue abstimmQueue = (Queue) context.lookup(Data.QUEUE_ABSTIMMUNG);
        abstimmReceiver = queueSession.createReceiver(abstimmQueue);

        Queue ergebnisQueue = (Queue) context.lookup(Data.QUEUE_ABFRAGE);
        ergebnisReceiver = queueSession.createReceiver(ergebnisQueue);

        TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) context.lookup("ConnectionFactory");
        TopicConnection topicConnection = topicConnectionFactory.createTopicConnection(Data.USER, Data.PASSWORD);
        umfrageSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic umfrageTopic = (Topic) context.lookup(Data.TOPIC_UMFRAGE);

        umfragePublisher = umfrageSession.createPublisher(umfrageTopic);
        queueConnection.start();
        topicConnection.start();
    }

    public static void main(String[] args) throws NamingException, JMSException {
        Server server = new Server();
    }

}
