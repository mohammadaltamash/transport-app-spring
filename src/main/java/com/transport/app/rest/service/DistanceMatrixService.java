package com.transport.app.rest.service;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Random;
import javafx.util.Pair;

@Service
public class DistanceMatrixService {

    @Value("${google.api.key}")
    private String API_KEY;
//	private static final GeoApiContext context = new GeoApiContext().setApiKey(API_KEY);
//
//
//	public DistanceMatrix estimateRouteTime(DateTime time, Boolean isForCalculateArrivalTime, DirectionsApi.RouteRestriction routeRestriction, LatLng departure, LatLng... arrivals) {
//		try {
//			DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(context);
//			if (isForCalculateArrivalTime) {
//				req.departureTime(time);
//			} else {
//				req.arrivalTime(time);
//			}
//			if (routeRestriction == null) {
//				routeRestriction = DirectionsApi.RouteRestriction.TOLLS;
//			}
//			DistanceMatrix trix = req.origins(departure)
//					.destinations(arrivals)
//					.mode(TravelMode.DRIVING)
//					.avoid(routeRestriction)
//					.language("fr-FR")
//					.await();
//			return trix;
//
//		} catch (ApiException e) {
//			System.out.println(e.getMessage());
//		} catch (Exception e) {
//			System.out.println(e.getMessage());
//		}
//		return null;
//	}
//
//	private static final String API_KEY="YOUR KEY";
//	OkHttpClient client = new OkHttpClient();
//	public String calculate(String source ,String destination) throws IOException {
//		String url="https://maps.googleapis.com/maps/api/distancematrix/json?origins="+source+"&destinations="+destination+"&key="+ API_KEY;
//		Request request = new Request.Builder()
//				.url(url)
//				.build();
//
//		Response response = client.newCall(request).execute();
//		return response.body().string();
//	}

    //	public static long getDriveDist(String addrOne, String addrTwo) throws ApiException, InterruptedException, IOException{
    public long getDriveDist(double lat1, double long1, double lat2, double long2) throws ApiException, InterruptedException, IOException {

        LatLng source = new LatLng(lat1, long1);
        LatLng destination = new LatLng(lat2, long2);
        //set up key
        GeoApiContext distCalcer = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();

        DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(distCalcer);
        DistanceMatrix result = req.origins(source)
                .destinations(destination)
                .mode(TravelMode.DRIVING)
//                .avoid(DirectionsApi.RouteRestriction.TOLLS)
//                .units(Unit.IMPERIAL)
                .language("en-US")
                .await();

        long distApart = result.rows[0].elements[0].distance.inMeters;

        return distApart;
    }

    public static Pair getLocation(double x0, double y0, int radius) {
        Random random = new Random();

        // Convert radius from meters to degrees
        double radiusInDegrees = radius / 111000f;

        double u = random.nextDouble();
        double v = random.nextDouble();
        double w = radiusInDegrees * Math.sqrt(u);
        double t = 2 * Math.PI * v;
        double x = w * Math.cos(t);
        double y = w * Math.sin(t);

        // Adjust the x-coordinate for the shrinking of the east-west distances
        double new_x = x / Math.cos(Math.toRadians(y0));

        double foundLatitude = new_x + x0;
        double foundLongitude = y + y0;
        System.out.println(foundLatitude + ", " + foundLongitude );
        return new Pair<>(foundLatitude, foundLongitude);
    }

    public static void main(String args[]) throws InterruptedException, ApiException, IOException {
        Pair pair = getLocation(34.0699334, -84.297443, 10000);
    }
}
