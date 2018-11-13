package cn.com.webservice.qq;

public class QqOnlineWebServiceSoapProxy implements cn.com.webservice.qq.QqOnlineWebServiceSoap {
  private String _endpoint = null;
  private cn.com.webservice.qq.QqOnlineWebServiceSoap qqOnlineWebServiceSoap = null;
  
  public QqOnlineWebServiceSoapProxy() {
    _initQqOnlineWebServiceSoapProxy();
  }
  
  public QqOnlineWebServiceSoapProxy(String endpoint) {
    _endpoint = endpoint;
    _initQqOnlineWebServiceSoapProxy();
  }
  
  private void _initQqOnlineWebServiceSoapProxy() {
    try {
      qqOnlineWebServiceSoap = (new cn.com.webservice.qq.QqOnlineWebServiceLocator()).getqqOnlineWebServiceSoap();
      if (qqOnlineWebServiceSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)qqOnlineWebServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)qqOnlineWebServiceSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (qqOnlineWebServiceSoap != null)
      ((javax.xml.rpc.Stub)qqOnlineWebServiceSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public cn.com.webservice.qq.QqOnlineWebServiceSoap getQqOnlineWebServiceSoap() {
    if (qqOnlineWebServiceSoap == null)
      _initQqOnlineWebServiceSoapProxy();
    return qqOnlineWebServiceSoap;
  }
  
  public java.lang.String qqCheckOnline(java.lang.String qqCode) throws java.rmi.RemoteException{
    if (qqOnlineWebServiceSoap == null)
      _initQqOnlineWebServiceSoapProxy();
    return qqOnlineWebServiceSoap.qqCheckOnline(qqCode);
  }
  
  
}