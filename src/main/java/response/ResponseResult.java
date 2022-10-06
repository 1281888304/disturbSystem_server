package response;

public class ResponseResult <D>{
  private String message;
  private D data;

  public ResponseResult(String message, D data) {
    this.message = message;
    this.data = data;
  }

  public ResponseResult(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public D getData() {
    return data;
  }

  public void setData(D data) {
    this.data = data;
  }
}
