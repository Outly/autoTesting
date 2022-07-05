package bookingApi;

public class ResponseBooking {
    public Integer bookingid;
    public RequestBooking booking;

    public ResponseBooking(Integer bookingid, RequestBooking booking) {
        this.bookingid = bookingid;
        this.booking = booking;
    }

    public ResponseBooking() {
    }

    public Integer getBookingid() {
        return bookingid;
    }

    public RequestBooking getBooking() {
        return booking;
    }

    @Override
    public String toString() {
        return "ResponseBooking{" +
                "bookingid=" + bookingid +
                ", booking=" + booking +
                '}';
    }
}
