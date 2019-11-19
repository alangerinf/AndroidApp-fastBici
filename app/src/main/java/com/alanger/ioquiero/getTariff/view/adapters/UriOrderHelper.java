package com.alanger.ioquiero.getTariff.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class UriOrderHelper {

    private String latStart,lonStart;   //Coordinates Start
    private String latEnd, lonEnd;      //Coordinates End

    //Client Info
    private String clientName;
    private String clientPhone;


    private String productDetail;

    private String addressStart;
    private String referenceStart;

    private String addressEnd;
    private String referenceEnd;

    private String observations;

    private String payMode;
    private String howMatch;

    private String time;
    private String price;



    public UriOrderHelper UriOrderHelper(){
        this.latStart = "";
        this.lonStart = "";
        this.latEnd = "";
        this.lonEnd = "";
        this.clientName = "";
        this.clientPhone = "";
        this.productDetail = "";
        this.addressStart = "";
        this.referenceStart = "";
        this.addressEnd = "";
        this.referenceEnd = "";
        this.payMode = "";
        this.howMatch = "";
        observations = "";
        return this;
    }




    public static void buildWhatsAppRequest(Context ctx, Uri uri) {

        Intent whatsappIntent = new Intent(Intent.ACTION_VIEW, uri);

        try {
            ctx.startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ctx,"Whatsapp no esta instalado.",Toast.LENGTH_LONG).show();
        }
    }

    // eText just to show error
    public boolean validate(EditText eTextClientPhone, EditText eTextClientName, EditText eTextProduct, EditText eTextRefStart, EditText eTextRefFinish) throws Exception {

        boolean isEmptyName = clientName.isEmpty();
        boolean isEmptyProduct = productDetail.isEmpty();
        boolean isEmptyRefFinish = referenceEnd.isEmpty();
        boolean isEmptyRefStart = referenceStart.isEmpty();
        boolean isInvalidPhone = clientPhone.length()!=9;


        if(latStart.isEmpty()){
            throw new NullPointerException("Falta latitud inicial");
        }
        if(lonStart.isEmpty()){
            throw new NullPointerException("Falta longitud inicial");
        }

        if(latEnd.isEmpty()){
            throw new NullPointerException("Falta latitud final");
        }
        if(lonEnd.isEmpty()){
            throw new NullPointerException("Falta longitud final");
        }

        if(addressStart.isEmpty()){
            throw new NullPointerException("Falta direccion inicial");
        }

        if(addressEnd.isEmpty()){
            throw new NullPointerException("Falta dirección final");
        }

        if(payMode.isEmpty()){
            throw new NullPointerException("Falta metodo de pago");
        }

        if(time.isEmpty()){
            throw new NullPointerException("Falta timpo de recorrido");
        }

        if(price.isEmpty()){
            throw new NullPointerException("Falta precio");
        }

        if(isInvalidPhone){
            eTextClientPhone.setError("Ingrese un numero de 9 digitos");
        }
        if(isEmptyName){
            eTextClientName.setError("Ingrese su nombre");
        }
        if(isEmptyProduct){
            eTextProduct.setError("Ingrese un producto");
        }
        if(isEmptyRefStart) {
            eTextRefStart.setError("Ingrese una referencia de recojo");
        }
        if(isEmptyRefFinish) {
            eTextRefFinish.setError("Ingrese una referencia de donde vamos");
        }

        return !(isEmptyProduct || isEmptyName|| isEmptyRefFinish || isEmptyRefStart ||  isInvalidPhone);
    }


    public boolean validate() {

        boolean isEmptyName = clientName.isEmpty();
        boolean isEmptyProduct = productDetail.isEmpty();
        boolean isEmptyRefFinish = referenceEnd.isEmpty();
        boolean isEmptyRefStart = referenceStart.isEmpty();
        boolean isInvalidPhone = clientPhone.length()!=9;


        if(latStart.isEmpty()){
            throw new NullPointerException("Falta latitud inicial");
        }
        if(lonStart.isEmpty()){
            throw new NullPointerException("Falta longitud inicial");
        }

        if(latEnd.isEmpty()){
            throw new NullPointerException("Falta latitud final");
        }
        if(lonEnd.isEmpty()){
            throw new NullPointerException("Falta longitud final");
        }

        if(addressStart.isEmpty()){
            throw new NullPointerException("Falta direccion inicial");
        }

        if(addressEnd.isEmpty()){
            throw new NullPointerException("Falta dirección final");
        }

        if(payMode.isEmpty()){
            throw new NullPointerException("Falta metodo de pago");
        }


        return !(isEmptyProduct || isEmptyName|| isEmptyRefFinish || isEmptyRefStart ||  isInvalidPhone);
    }



    public Uri makeUriByPhone(String hisPhone){
        String uriGoogleMaps = "http://maps.google.com/?mode=walking%26saddr=" + latStart + "," + lonStart + "%26daddr=" + latEnd + "," + lonEnd;

        /**
         ENTREGA PEDIDO ENCARACOLADOS
         HORA DE ENTREGA 12 AM
         CLIENTE: KARINA FLORES
         CELULAR: 948051691
         DIRECCION: CALLE LOS BRILLANTES 627, RESIDENCIAL LOS BRILLANTES BLOCK H DPTO 404, URB SANTA INÉS
         COBRAR: 99.00 SOLES INCLUIDO DELIVERY
         COSTO DEL DELIVERY 9.00
         OBSERVACION: CLIENTE PAGA CON 100.00 SOLES
         UBICACION
         */


        /******************************* */

        return Uri.parse(
                "https://api.whatsapp.com/send?phone=" + hisPhone + "&text=" +
                        "\n*___________ ENTREGA PEDIDO ___________*"+
                        "\n*NOMBRE:*"                         +" "+clientName.trim() +
                        "\n*TELEFONO:*"                       +" "+clientPhone.trim() +
                        "\n*DESCRIPCIÓN DEL PRODUCTO:*"     +" "+productDetail.trim()+
                        "\n*TIEMPO APROXIMADO:*"              +" "+time   +" MINUTOS"+
                        "\n*COSTO DEL DELIVERY:*"             +" "+ price +" SOLES" +
                        "\n"+
                        "\n*¿DONDE RECOJEMOS?:*"     +
                        "\n "+ addressStart +
                        "\n"+
                        (referenceStart.trim().isEmpty()?
                                ""
                                :
                                "*_REFERENCIA DE DONDE RECOJEMOS:_*"+
                                        "\n"+referenceStart.trim()
                        ) +
                        "\n*¿DONDE VAMOS?:*" +
                        "\n "+ addressEnd +
                        "\n" +
                        (referenceEnd.trim().isEmpty()?
                                ""
                                :
                                "*_REFERENCIA DE DONDE VAMOS:_*"+
                                        "\n"+referenceEnd.trim()

                        )+
                        "\n"+
                        (observations.trim().isEmpty()?
                                ""
                                :
                                "*OBSERVACIONES:*"+
                                        "\n"+observations.trim()
                        )+
                        "\n"+
                        (howMatch.trim().isEmpty()?
                                ""
                                :
                                "*Paga con:* S/ "+ howMatch.trim()+" ( *"+ payMode +"* ) "
                        )+
                        "\n"+
                        uriGoogleMaps
                )
                ;
    }


    public UriOrderHelper putLatStart(String coodinate){
        this.latStart = coodinate;
        return this;
    }

    public UriOrderHelper putLonStart(String coodinate){
        this.lonStart = coodinate;
        return this;
    }

    public UriOrderHelper putLatEnd(String coodinate){
        this.latEnd = coodinate;
        return this;
    }

    public UriOrderHelper putLonEnd(String coodinate){
        this.lonEnd = coodinate;
        return this;
    }

    public UriOrderHelper putClientName(String name){
        this.clientName = name;
        return this;
    }

    public UriOrderHelper putClientPhone(String phone){
        this.clientPhone = phone;
        return this;
    }

    public UriOrderHelper putProductDetail(String product){
        this.productDetail = product;
        return this;
    }

    public UriOrderHelper putAddressStart(String address){
        this.addressStart = address;
        return this;
    }

    public UriOrderHelper putReferenceStart(String reference){
        this.referenceStart = reference;
        return this;
    }


    public UriOrderHelper putAddressEnd(String address){
        this.addressEnd = address;
        return this;
    }

    public UriOrderHelper putReferenceEnd(String reference){
        this.referenceEnd = reference;
        return this;
    }

    public UriOrderHelper putPayMode(String mode){
        this.payMode = mode;
        return this;
    }

    public UriOrderHelper putHowMatch(String howMatch){
        this.howMatch = howMatch;
        return this;
    }

    public UriOrderHelper putPrice(String price){
        this.price = price;
        return this;
    }

    public UriOrderHelper putTime(String time){
        this.time = time;
        return this;
    }

    public UriOrderHelper putObservations(String obs){
        this.observations = obs;
        return this;
    }

    public String getLatStart() {
        return latStart;
    }

    public String getLonStart() {
        return lonStart;
    }

    public String getLatEnd() {
        return latEnd;
    }

    public String getLonEnd() {
        return lonEnd;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public String getAddressStart() {
        return addressStart;
    }

    public String getReferenceStart() {
        return referenceStart;
    }

    public String getAddressEnd() {
        return addressEnd;
    }

    public String getReferenceEnd() {
        return referenceEnd;
    }

    public String getObservations() {
        return observations;
    }

    public String getPayMode() {
        return payMode;
    }

    public String getHowMatch() {
        return howMatch;
    }

    public String getTime() {
        return time;
    }

    public String getPrice() {
        return price;
    }
}
