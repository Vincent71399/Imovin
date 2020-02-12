package sg.edu.nus.imovin.Retrofit.Object;

import java.util.List;

public class MessageData {
    private List<MessageItemData> Monitor;
    private List<MessageItemData> Reminder;
    private List<MessageItemData> Rewards;
    private List<MessageItemData> challenge;

    public List<MessageItemData> getMonitor() {
        return Monitor;
    }

    public void setMonitor(List<MessageItemData> monitor) {
        Monitor = monitor;
    }

    public List<MessageItemData> getReminder() {
        return Reminder;
    }

    public void setReminder(List<MessageItemData> reminder) {
        Reminder = reminder;
    }

    public List<MessageItemData> getRewards() {
        return Rewards;
    }

    public void setRewards(List<MessageItemData> rewards) {
        Rewards = rewards;
    }

    public List<MessageItemData> getChallenge() {
        return challenge;
    }

    public void setChallenge(List<MessageItemData> challenge) {
        this.challenge = challenge;
    }

}
