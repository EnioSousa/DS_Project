package ds.trabalho.parte3;

import java.util.Comparator;

public class ChatMessage implements Comparator<ChatMessage> {
    int timeStamp;
    int machineId;
    String content;

    public ChatMessage(int timeStamp, int machineId, String content) {
	super();
	this.timeStamp = timeStamp;
	this.machineId = machineId;
	this.content = content;
    }

    public ChatMessage(String timeStamp, String machineId, String content) {
	super();
	this.timeStamp = Integer.parseInt(timeStamp);
	this.machineId = Integer.parseInt(machineId);
	this.content = content;
    }

    public int getTimeStamp() {
	return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
	this.timeStamp = timeStamp;
    }

    public int getMachineId() {
	return machineId;
    }

    public void setMachineId(int machineId) {
	this.machineId = machineId;
    }

    public String getContent() {
	return content;
    }

    public void setContent(String content) {
	this.content = content;
    }

    @Override
    public int compare(ChatMessage o1, ChatMessage o2) {
	return o1.getTimeStamp() == (o2.getTimeStamp())
		? o1.getMachineId() - o2.getMachineId()
		: o1.getTimeStamp() - o2.getTimeStamp();

    }

}
