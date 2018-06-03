package co.osg.tutorial.messaging.workflows;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Application {

    public static void main(String[] args) throws JMSException {
        Connection connection = null;
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://embedded");
        try {
            // Producer
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            Queue queue = session.createQueue("customerQueue");
            MessageProducer producer = session.createProducer(queue);
            String payload = "SomeTask";
            Message msg = session.createTextMessage(payload);
            System.out.println("Sending text '" + payload + "'");
            producer.send(msg);
            session.close();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false,
                Session.CLIENT_ACKNOWLEDGE);
        TextMessage textMsg;
        try {
            Queue queue = session.createQueue("customerQueue");

            // Consumer
            MessageConsumer consumer = session.createConsumer(queue);
            textMsg = (TextMessage) consumer.receive();
            System.out.println(textMsg);
            System.out.println("Received: " + textMsg.getText());
            textMsg.acknowledge();
        } finally {
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
        }


    }
}
