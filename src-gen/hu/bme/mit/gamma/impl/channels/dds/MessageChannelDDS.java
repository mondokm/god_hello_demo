package hu.bme.mit.gamma.impl.channels.dds;

import java.util.LinkedList;
import java.util.List;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.domain.DomainParticipantFactoryQos;
import com.rti.dds.domain.DomainParticipantQos;
import com.rti.dds.infrastructure.InstanceHandle_t;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.infrastructure.ResourceLimitsQosPolicy;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.Publisher;
import com.rti.dds.subscription.DataReader;
import com.rti.dds.subscription.DataReaderAdapter;
import com.rti.dds.subscription.DataReaderListener;
import com.rti.dds.subscription.InstanceStateKind;
import com.rti.dds.subscription.SampleInfo;
import com.rti.dds.subscription.SampleInfoSeq;
import com.rti.dds.subscription.SampleStateKind;
import com.rti.dds.subscription.Subscriber;
import com.rti.dds.subscription.ViewStateKind;
import com.rti.dds.topic.Topic;

import ddsfiles.Event;
import ddsfiles.EventDataReader;
import ddsfiles.EventDataWriter;
import ddsfiles.EventSeq;
import ddsfiles.EventTypeSupport;

public class MessageChannelDDS {

	DomainParticipant participant;
    Topic topic;
    
    Publisher publisher;
    Subscriber subscriber;
    
    EventDataWriter writer;
    DataReaderListener listener;
    EventDataReader reader;
    
    private List<MessageListener> listeners = new LinkedList<MessageListener>();
    private String topicName;
    
    private String qosFilePath="";
    private String qosLibrary="";
    
    public MessageChannelDDS(String topicName, String qosFilePath, String qosLibrary) {
       	this.topicName=topicName;
       	this.qosFilePath=qosFilePath;
       	this.qosLibrary=qosLibrary;
    }
    
    public void setupWriter() {
    		
        participant = DomainParticipantFactory.TheParticipantFactory.
        create_participant(
            0, DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT,
            null /* listener */, StatusKind.STATUS_MASK_NONE);
        if (participant == null) {
            System.err.println("create_participant error\n");
            return;
        }        

        publisher = participant.create_publisher(
            DomainParticipant.PUBLISHER_QOS_DEFAULT, null /* listener */,
            StatusKind.STATUS_MASK_NONE);
        if (publisher == null) {
            System.err.println("create_publisher error\n");
            return;
        }                   

        String typeName = EventTypeSupport.get_type_name();
        EventTypeSupport.register_type(participant, typeName);

        topic = participant.create_topic(
            topicName,
            typeName, DomainParticipant.TOPIC_QOS_DEFAULT,
            null /* listener */, StatusKind.STATUS_MASK_NONE);
        if (topic == null) {
            System.err.println("create_topic error\n");
            return;
        }           

        writer = (EventDataWriter)
        publisher.create_datawriter(
            topic, Publisher.DATAWRITER_QOS_DEFAULT,
            null /* listener */, StatusKind.STATUS_MASK_NONE);
        if (writer == null) {
            System.err.println("create_datawriter error\n");
            return;
        }           
        
    }
    
    public void setupWriter(String qosProfile) {
    	
		DomainParticipantFactoryQos factoryQos = new DomainParticipantFactoryQos();
		DomainParticipantFactory.TheParticipantFactory.get_qos(factoryQos);
		
		factoryQos.profile.url_profile.add("file://"+qosFilePath);
		DomainParticipantFactory.TheParticipantFactory.set_qos(factoryQos);
		
	    participant = DomainParticipantFactory.TheParticipantFactory.
	    create_participant_with_profile(
	        0, qosLibrary, qosProfile,
	        null /* listener */, StatusKind.STATUS_MASK_NONE);
	    if (participant == null) {
	        System.err.println("create_participant error\n");
	        return;
	    }        
	
	    publisher = participant.create_publisher_with_profile(
	    	qosLibrary,	qosProfile, null /* listener */,
	        StatusKind.STATUS_MASK_NONE);
	    if (publisher == null) {
	        System.err.println("create_publisher error\n");
	        return;
	    }                   
	
	    String typeName = EventTypeSupport.get_type_name();
	    EventTypeSupport.register_type(participant, typeName);
	
	    topic = participant.create_topic_with_profile(
	        topicName,
	        typeName, qosLibrary, qosProfile,
	        null /* listener */, StatusKind.STATUS_MASK_NONE);
	    if (topic == null) {
	        System.err.println("create_topic error\n");
	        return;
	    }           
	
	    writer = (EventDataWriter)
	    publisher.create_datawriter_with_profile(
	        topic, qosLibrary,	qosProfile,
	        null /* listener */, StatusKind.STATUS_MASK_NONE);
	    if (writer == null) {
	        System.err.println("create_datawriter error\n");
	        return;
	    }           
	    
    }
    
    public void setupReader() {
	
        participant = DomainParticipantFactory.TheParticipantFactory.
        create_participant(
            0, DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT,
            null /* listener */, StatusKind.STATUS_MASK_NONE);
        if (participant == null) {
            System.err.println("create_participant error\n");
            return;
        }                         

        subscriber = participant.create_subscriber(
            DomainParticipant.SUBSCRIBER_QOS_DEFAULT, null /* listener */,
            StatusKind.STATUS_MASK_NONE);
        if (subscriber == null) {
            System.err.println("create_subscriber error\n");
            return;
        }     

        String typeName = EventTypeSupport.get_type_name(); 
        EventTypeSupport.register_type(participant, typeName);

        topic = participant.create_topic(
            topicName,
            typeName, DomainParticipant.TOPIC_QOS_DEFAULT,
            null /* listener */, StatusKind.STATUS_MASK_NONE);
        if (topic == null) {
            System.err.println("create_topic error\n");
            return;
        }                     

        listener = new EventListener();

        reader = (EventDataReader)
        subscriber.create_datareader(
            topic, Subscriber.DATAREADER_QOS_DEFAULT, listener,
            StatusKind.STATUS_MASK_ALL);
        if (reader == null) {
            System.err.println("create_datareader error\n");
            return;
        }                         

    }
    
    public void setupReader(String qosProfile) {
        	
        	DomainParticipantFactoryQos factoryQos = new DomainParticipantFactoryQos();
        	DomainParticipantFactory.TheParticipantFactory.get_qos(factoryQos);
        	
        	factoryQos.profile.url_profile.add("file://"+qosFilePath);
        	DomainParticipantFactory.TheParticipantFactory.set_qos(factoryQos);
        	
            participant = DomainParticipantFactory.TheParticipantFactory.
            create_participant_with_profile(
                0, qosLibrary, qosProfile,
                null /* listener */, StatusKind.STATUS_MASK_NONE);
            if (participant == null) {
                System.err.println("create_participant error\n");
                return;
            }                         
    
            subscriber = participant.create_subscriber_with_profile(
            	qosLibrary, qosProfile, null /* listener */,
                StatusKind.STATUS_MASK_NONE);
            if (subscriber == null) {
                System.err.println("create_subscriber error\n");
                return;
            }     
    
            String typeName = EventTypeSupport.get_type_name(); 
            EventTypeSupport.register_type(participant, typeName);
    
            topic = participant.create_topic_with_profile(
                topicName,
                typeName, qosLibrary, qosProfile,
                null /* listener */, StatusKind.STATUS_MASK_NONE);
            if (topic == null) {
                System.err.println("create_topic error\n");
                return;
            }                     
    
            listener = new EventListener();
    
            reader = (EventDataReader)
            subscriber.create_datareader_with_profile(
                topic, qosLibrary, qosProfile, listener,
                StatusKind.STATUS_MASK_ALL);
            if (reader == null) {
                System.err.println("create_datareader error\n");
                return;
            }                         
    
        }
    
    
    //methods for raising events
    public void raiseHello() {
	
    	Event instance = new Event();
    	instance.name="hello";
    	
    	InstanceHandle_t instance_handle = InstanceHandle_t.HANDLE_NIL;
        
        writer.write(instance, instance_handle);
            
    }
    
    private class EventListener extends DataReaderAdapter {

        EventSeq _dataSeq = new EventSeq();
        SampleInfoSeq _infoSeq = new SampleInfoSeq();

        public void on_data_available(DataReader reader) {
            EventDataReader EventReader =
            (EventDataReader)reader;

            try {
                EventReader.take(
                    _dataSeq, _infoSeq,
                    ResourceLimitsQosPolicy.LENGTH_UNLIMITED,
                    SampleStateKind.ANY_SAMPLE_STATE,
                    ViewStateKind.ANY_VIEW_STATE,
                    InstanceStateKind.ANY_INSTANCE_STATE);

                SampleInfo info = (SampleInfo)_infoSeq.get(0);

                if (info.valid_data) {
                    switch(((Event)_dataSeq.get(0)).name) {
                    	case "hello": for(MessageListener listener:listeners) {listener.raisedHello();}
                    	break;
                    }
                }

            } catch (RETCODE_NO_DATA noData) {
                // No data to process
            } finally {
                EventReader.return_loan(_dataSeq, _infoSeq);
            }
        }
    }
    
    public interface MessageListener{
    	public void raisedHello();
    }
    
    public void addSubscriptionListener(MessageListener listener) {
    	listeners.add(listener);
    }
    
    public void closeTopic() {
    	if(participant != null) {
            participant.delete_contained_entities();

            DomainParticipantFactory.TheParticipantFactory.
            delete_participant(participant);
        }
    }
	
}

