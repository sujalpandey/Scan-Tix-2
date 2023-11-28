package com.project.scantix;
import android.graphics.Bitmap;
import android.graphics.Color;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

public class Ticket {
    private String eventName;
    private String eventDate;
    private String ticketNo;
    private String userName;
    private String eventVenue;

    public Ticket(String eventName, String eventDate, String ticketNo, String userName, String eventVenue) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.ticketNo = ticketNo;
        this.userName = userName;
        this.eventVenue = eventVenue;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public String getEventDate() {
        return eventDate;
    }

    public String getTicketNo() {
        return ticketNo;
    }

    public String getUserName() {
        return userName;
    }

    // Generate the QR code bitmap for the ticket
    public Bitmap generateQrCodeBitmap() {
        try {
            // Construct the content for the QR code
            String qrCodeContent = "Event: " + eventName + "\nDate: " + eventDate + "\nTicket Number: " + ticketNo;

            // Set up the QR code parameters
            int width = 300; // Set your preferred width
            int height = 300; // Set your preferred height
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                    qrCodeContent,
                    BarcodeFormat.QR_CODE,
                    width,
                    height,
                    null
            );

            // Create a bitmap from the BitMatrix
            Bitmap qrCodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    qrCodeBitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            return qrCodeBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Handle the exception accordingly
        }
    }
}
