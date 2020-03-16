package sg.edu.nus.imovin2.Retrofit.Request;

public class DailyLogRequest {
    private String date;
    private Integer home;
    private Integer challenge;
    private Integer library;
    private Integer social;
    private Integer forum;
    private Integer monitor;
    private Integer goal;

    public DailyLogRequest(String date, Integer home, Integer challenge, Integer library, Integer social, Integer forum, Integer monitor, Integer goal) {
        this.date = date;
        this.home = home;
        this.challenge = challenge;
        this.library = library;
        this.social = social;
        this.forum = forum;
        this.monitor = monitor;
        this.goal = goal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getHome() {
        return home;
    }

    public void setHome(Integer home) {
        this.home = home;
    }

    public Integer getChallenge() {
        return challenge;
    }

    public void setChallenge(Integer challenge) {
        this.challenge = challenge;
    }

    public Integer getLibrary() {
        return library;
    }

    public void setLibrary(Integer library) {
        this.library = library;
    }

    public Integer getSocial() {
        return social;
    }

    public void setSocial(Integer social) {
        this.social = social;
    }

    public Integer getForum() {
        return forum;
    }

    public void setForum(Integer forum) {
        this.forum = forum;
    }

    public Integer getMonitor() {
        return monitor;
    }

    public void setMonitor(Integer monitor) {
        this.monitor = monitor;
    }

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }
}
