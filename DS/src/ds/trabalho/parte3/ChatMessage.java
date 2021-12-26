package ds.trabalho.parte3;

public class ChatMessage implements Comparable<ChatMessage> {
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
    public int compareTo(ChatMessage o) {
	return timeStamp == o.getTimeStamp() ? machineId - o.getMachineId()
		: timeStamp - o.getTimeStamp();
    }

}
