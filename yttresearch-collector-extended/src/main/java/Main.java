import twitter4j.*;
import twitter4j.auth.AccessToken;

/**
 * Created by zgeorg03 on 4/26/17.
 */
public class Main {

    public static void main(String args[]){

        TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
        twitterStream.setOAuthConsumer("14rehezR4o1ZPhlj0Jar1BgdO","JXil4LwdVR5PKytRHNIZns9KxIxz1DR3eU6MPB2EXRS6gTJsQK");
        twitterStream.setOAuthAccessToken( new AccessToken("386663088-KcWCB3DcUaGht4AWiMEvAOUSxMavBtKBziT3COzw","Hy7upbbNWlVcN2EnKsiTj4Mp0xswHGkpiwCYdT3KpyKrZ"));


        twitterStream.addListener(new StatusListener() {
            @Override
            public void onStatus(Status status) {


                Place place = status.getPlace();
                Place[] pl = place.getContainedWithIn();
                if(place==null){
                    System.out.println("Place not available");
                    return;
                }
                GeoLocation[][] array = place.getBoundingBoxCoordinates();
                GeoLocation[] coordinates = array[0];

                GeoLocation downleft = coordinates[0];
                GeoLocation upLeft = coordinates[1];
                GeoLocation upRight = coordinates[2];
                GeoLocation downRight = coordinates[3];

                GeoLocation dl = array[0][0];

                System.out.println(status.getText()+":"+place.getFullName()+":" + coordinates[0].getLatitude()+","+coordinates[0].getLongitude());
                URLEntity urls[] = status.getURLEntities();
                for (URLEntity url : urls) {
                    String expandedUrl = url.getExpandedURL();
                    //Make sure that is a youtube link
                    if (!YoutubeTool.applyYoutubeFilter(expandedUrl))
                        continue;
                    String videoId = YoutubeTool.extractID(expandedUrl);
                    System.out.println(status.getText()+"\t"+videoId);
                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {

            }

            @Override
            public void onStallWarning(StallWarning warning) {

            }

            @Override
            public void onException(Exception ex) {

            }
        });
        double lat = 35.14;
        double lon = 33.37;

        double lon1 = lon - 2; // left
        double lon2 = lon + 2.; // right
        double lat1 = lat - 2.; // bottom
        double lat2 = lat + 2.; //up

        double box[][] = {{lon1, lat1}, {lon2, lat2}};
        FilterQuery query = new FilterQuery().locations(box);
        twitterStream.filter(query);

    }
}
