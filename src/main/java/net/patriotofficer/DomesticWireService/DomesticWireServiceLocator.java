/**
 * DomesticWireServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package net.patriotofficer.DomesticWireService;

public class DomesticWireServiceLocator extends org.apache.axis.client.Service implements net.patriotofficer.DomesticWireService.DomesticWireService {

	private static final long serialVersionUID = 1L;

	public DomesticWireServiceLocator() {
    }


    public DomesticWireServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DomesticWireServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for BasicHttpBinding_IDomesticWireService
    private java.lang.String BasicHttpBinding_IDomesticWireService_address = "http://s0021amlts04:4445/DomesticWire.svc";

    public java.lang.String getBasicHttpBinding_IDomesticWireServiceAddress() {
        return BasicHttpBinding_IDomesticWireService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String BasicHttpBinding_IDomesticWireServiceWSDDServiceName = "BasicHttpBinding_IDomesticWireService";

    public java.lang.String getBasicHttpBinding_IDomesticWireServiceWSDDServiceName() {
        return BasicHttpBinding_IDomesticWireServiceWSDDServiceName;
    }

    public void setBasicHttpBinding_IDomesticWireServiceWSDDServiceName(java.lang.String name) {
        BasicHttpBinding_IDomesticWireServiceWSDDServiceName = name;
    }

    public net.patriotofficer.DomesticWireService.IDomesticWireService getBasicHttpBinding_IDomesticWireService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(BasicHttpBinding_IDomesticWireService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getBasicHttpBinding_IDomesticWireService(endpoint);
    }

    public net.patriotofficer.DomesticWireService.IDomesticWireService getBasicHttpBinding_IDomesticWireService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            net.patriotofficer.DomesticWireService.BasicHttpBinding_IDomesticWireServiceStub _stub = new net.patriotofficer.DomesticWireService.BasicHttpBinding_IDomesticWireServiceStub(portAddress, this);
            _stub.setPortName(getBasicHttpBinding_IDomesticWireServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setBasicHttpBinding_IDomesticWireServiceEndpointAddress(java.lang.String address) {
        BasicHttpBinding_IDomesticWireService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (net.patriotofficer.DomesticWireService.IDomesticWireService.class.isAssignableFrom(serviceEndpointInterface)) {
                net.patriotofficer.DomesticWireService.BasicHttpBinding_IDomesticWireServiceStub _stub = new net.patriotofficer.DomesticWireService.BasicHttpBinding_IDomesticWireServiceStub(new java.net.URL(BasicHttpBinding_IDomesticWireService_address), this);
                _stub.setPortName(getBasicHttpBinding_IDomesticWireServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("BasicHttpBinding_IDomesticWireService".equals(inputPortName)) {
            return getBasicHttpBinding_IDomesticWireService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://patriotofficer.net/DomesticWireService", "DomesticWireService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://patriotofficer.net/DomesticWireService", "BasicHttpBinding_IDomesticWireService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("BasicHttpBinding_IDomesticWireService".equals(portName)) {
            setBasicHttpBinding_IDomesticWireServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
