/**
 * DomesticWireResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.datacontract.schemas._2004._07.PatriotOfficer_DomesticWireService;

public class DomesticWireResult implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private java.lang.String AMLReference;

	private java.lang.String matchedResult;

	private java.math.BigDecimal RCScore;

	private java.lang.String referenceNumber;

	public DomesticWireResult() {
	}

	public DomesticWireResult(
			java.lang.String AMLReference,
			java.lang.String matchedResult,
			java.math.BigDecimal RCScore,
			java.lang.String referenceNumber) {
		this.AMLReference = AMLReference;
		this.matchedResult = matchedResult;
		this.RCScore = RCScore;
		this.referenceNumber = referenceNumber;
	}

	/**
	 * Gets the AMLReference value for this DomesticWireResult.
	 * 
	 * @return AMLReference
	 */
	public java.lang.String getAMLReference() {
		return AMLReference;
	}

	/**
	 * Sets the AMLReference value for this DomesticWireResult.
	 * 
	 * @param AMLReference
	 */
	public void setAMLReference(java.lang.String AMLReference) {
		this.AMLReference = AMLReference;
	}

	/**
	 * Gets the matchedResult value for this DomesticWireResult.
	 * 
	 * @return matchedResult
	 */
	public java.lang.String getMatchedResult() {
		return matchedResult;
	}

	/**
	 * Sets the matchedResult value for this DomesticWireResult.
	 * 
	 * @param matchedResult
	 */
	public void setMatchedResult(java.lang.String matchedResult) {
		this.matchedResult = matchedResult;
	}

	/**
	 * Gets the RCScore value for this DomesticWireResult.
	 * 
	 * @return RCScore
	 */
	public java.math.BigDecimal getRCScore() {
		return RCScore;
	}

	/**
	 * Sets the RCScore value for this DomesticWireResult.
	 * 
	 * @param RCScore
	 */
	public void setRCScore(java.math.BigDecimal RCScore) {
		this.RCScore = RCScore;
	}

	/**
	 * Gets the referenceNumber value for this DomesticWireResult.
	 * 
	 * @return referenceNumber
	 */
	public java.lang.String getReferenceNumber() {
		return referenceNumber;
	}

	/**
	 * Sets the referenceNumber value for this DomesticWireResult.
	 * 
	 * @param referenceNumber
	 */
	public void setReferenceNumber(java.lang.String referenceNumber) {
		this.referenceNumber = referenceNumber;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof DomesticWireResult))
			return false;
		DomesticWireResult other = (DomesticWireResult) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals = true &&
				((this.AMLReference == null && other.getAMLReference() == null) ||
						(this.AMLReference != null &&
								this.AMLReference.equals(other.getAMLReference())))
				&&
				((this.matchedResult == null && other.getMatchedResult() == null) ||
						(this.matchedResult != null &&
								this.matchedResult.equals(other.getMatchedResult())))
				&&
				((this.RCScore == null && other.getRCScore() == null) ||
						(this.RCScore != null &&
								this.RCScore.equals(other.getRCScore())))
				&&
				((this.referenceNumber == null && other.getReferenceNumber() == null) ||
						(this.referenceNumber != null &&
								this.referenceNumber.equals(other.getReferenceNumber())));
		__equalsCalc = null;
		return _equals;
	}

	private boolean __hashCodeCalc = false;

	public synchronized int hashCode() {
		if (__hashCodeCalc) {
			return 0;
		}
		__hashCodeCalc = true;
		int _hashCode = 1;
		if (getAMLReference() != null) {
			_hashCode += getAMLReference().hashCode();
		}
		if (getMatchedResult() != null) {
			_hashCode += getMatchedResult().hashCode();
		}
		if (getRCScore() != null) {
			_hashCode += getRCScore().hashCode();
		}
		if (getReferenceNumber() != null) {
			_hashCode += getReferenceNumber().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

	// Type metadata
	private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(DomesticWireResult.class, true);

	static {
		typeDesc.setXmlType(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/PatriotOfficer.DomesticWireService", "DomesticWireResult"));
		org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("AMLReference");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/PatriotOfficer.DomesticWireService", "AMLReference"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("matchedResult");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/PatriotOfficer.DomesticWireService", "MatchedResult"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("RCScore");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/PatriotOfficer.DomesticWireService", "RCScore"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
		elemField.setMinOccurs(0);
		elemField.setNillable(false);
		typeDesc.addFieldDesc(elemField);
		elemField = new org.apache.axis.description.ElementDesc();
		elemField.setFieldName("referenceNumber");
		elemField.setXmlName(new javax.xml.namespace.QName("http://schemas.datacontract.org/2004/07/PatriotOfficer.DomesticWireService", "ReferenceNumber"));
		elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
		elemField.setMinOccurs(0);
		elemField.setNillable(true);
		typeDesc.addFieldDesc(elemField);
	}

	/**
	 * Return type metadata object
	 */
	public static org.apache.axis.description.TypeDesc getTypeDesc() {
		return typeDesc;
	}

	/**
	 * Get Custom Serializer
	 */
	public static org.apache.axis.encoding.Serializer getSerializer(
			java.lang.String mechType,
			java.lang.Class _javaType,
			javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanSerializer(
				_javaType, _xmlType, typeDesc);
	}

	/**
	 * Get Custom Deserializer
	 */
	public static org.apache.axis.encoding.Deserializer getDeserializer(
			java.lang.String mechType,
			java.lang.Class _javaType,
			javax.xml.namespace.QName _xmlType) {
		return new org.apache.axis.encoding.ser.BeanDeserializer(
				_javaType, _xmlType, typeDesc);
	}

	@Override
	public String toString() {
		return org.apache.commons.lang3.builder.ReflectionToStringBuilder
				.toString(this, org.apache.commons.lang3.builder.ToStringStyle.MULTI_LINE_STYLE);
	}
}
