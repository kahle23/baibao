package misaka.app.push;

public interface PushProvider {

    PushResult send(PushMessage pushMessage);

}
