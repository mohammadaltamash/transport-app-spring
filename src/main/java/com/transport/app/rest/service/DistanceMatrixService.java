package com.transport.app.rest.service;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

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

        int length = result.rows.length;
        DistanceMatrixRow row0 = result.rows[0];
        int elementsLength = row0.elements.length;
        DistanceMatrixElement elements0 = row0.elements[0];
//        long distApart = result.rows[0].elements[0].distance.inMeters;
//        long distApart = elements0.distance.inMeters;
        long distApart = 0;
        
        if (result.rows[0].elements[0] != null && result.rows[0].elements[0].distance != null) {
            distApart = result.rows[0].elements[0].distance.inMeters;
        }
        return distApart;
    }
}
