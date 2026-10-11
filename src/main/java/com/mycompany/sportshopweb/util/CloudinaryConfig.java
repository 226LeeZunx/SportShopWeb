/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.sportshopweb.util;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
/**
 *
 * @author dung2
 */
public class CloudinaryConfig {
    private static Cloudinary cloudinary;
    public static Cloudinary getInstance(){
        if (cloudinary==null){
            cloudinary=new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "hcsitrto",
            "api_key", "127958914966143",
            "api_secret", "0LIlXSTBOnUt0aoZ3NVAvyTRjTU",
            "secure", true));
        }
        return cloudinary;
    }
    
    
}
