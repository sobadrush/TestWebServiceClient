package net.patriotofficer.DomesticWireService;

public class IDomesticWireServiceProxy implements net.patriotofficer.DomesticWireService.IDomesticWireService {
  private String _endpoint = null;
  private net.patriotofficer.DomesticWireService.IDomesticWireService iDomesticWireService = null;
  
  public IDomesticWireServiceProxy() {
    _initIDomesticWireServiceProxy();
  }
  
  public IDomesticWireServiceProxy(String endpoint) {
    _endpoint = endpoint;
    _initIDomesticWireServiceProxy();
  }
  
  private void _initIDomesticWireServiceProxy() {
    try {
      iDomesticWireService = (new net.patriotofficer.DomesticWireService.DomesticWireServiceLocator()).getBasicHttpBinding_IDomesticWireService();
      if (iDomesticWireService != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)iDomesticWireService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)iDomesticWireService)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (iDomesticWireService != null)
      ((javax.xml.rpc.Stub)iDomesticWireService)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public net.patriotofficer.DomesticWireService.IDomesticWireService getIDomesticWireService() {
    if (iDomesticWireService == null)
      _initIDomesticWireServiceProxy();
    return iDomesticWireService;
  }
  
  public org.datacontract.schemas._2004._07.PatriotOfficer_DomesticWireService.DomesticWireResult submitDomesticWire(org.datacontract.schemas._2004._07.PatriotOfficer_DomesticWireService.DomesticWireParameter params) throws java.rmi.RemoteException{
    if (iDomesticWireService == null)
      _initIDomesticWireServiceProxy();
    return iDomesticWireService.submitDomesticWire(params);
  }
  
  
}