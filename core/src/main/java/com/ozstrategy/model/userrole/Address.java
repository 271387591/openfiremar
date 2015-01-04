package com.ozstrategy.model.userrole;

import com.ozstrategy.model.AbstractBaseObject;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;
public class Address extends AbstractBaseObject implements Serializable {
  private static final long serialVersionUID = 3617859655330969141L;
  private String address;
  private String city;
  private String country;
  private String postalCode;
  private String province;

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Address)) {
      return false;
    }

    final Address address1 = (Address) o;

    return this.hashCode() == address1.hashCode();
  }

  public String getAddress() {
    return address;
  }

  public String getCity() {
    return city;
  }

  public String getCountry() {
    return country;
  }

  public String getPostalCode() {
    return postalCode;
  }
  public String getProvince() {
    return province;
  }

  @Override public int hashCode() {
    int result;
    result = ((address != null) ? address.hashCode() : 0);
    result = (29 * result) + ((city != null) ? city.hashCode() : 0);
    result = (29 * result) + ((province != null) ? province.hashCode() : 0);
    result = (29 * result) + ((country != null) ? country.hashCode() : 0);
    result = (29 * result) + ((postalCode != null) ? postalCode.hashCode() : 0);

    return result;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public void setProvince(String province) {
    this.province = province;
  }

  @Override public String toString() {
    return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("country", this.country).append("address",
        this.address).append("province", this.province).append("postalCode", this.postalCode).append("city", this.city)
      .toString();
  }
} 
