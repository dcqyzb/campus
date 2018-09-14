package com.mit.campus.modular.system.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.mit.campus.config.properties.CampusProperties;
import com.mit.campus.core.util.FileUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * 
* 验证码生成
* @author shuyy
* @date 2018年9月12日
* @company mitesofor
 */
@Controller
@RequestMapping("/kaptcha")
@Api(value="验证码生成",tags={"验证码生成"})
public class KaptchaController {

    @Autowired
    private CampusProperties campusProperties;

    @Autowired
    private Producer producer;

    /**
     * 
    * 
    *  生成验证码
    * @param @param request
    * @param @param response
    * @return void
    * @throws
    * @company mitesofor
    * @author shuyy
     */
    @RequestMapping("")
    @ApiOperation(value="生成验证码", notes="生成验证码")
    public void index(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        response.setDateHeader("Expires", 0);

        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");

        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");

        // return a jpeg
        response.setContentType("image/jpeg");

        // create the text for the image
        String capText = producer.createText();

        // store the text in the session
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);

        // create the image with the text
        BufferedImage bi = producer.createImage(capText);
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // write the data out
        try {
            ImageIO.write(bi, "jpg", out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            try {
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 返回图片
     */
    @RequestMapping("/{pictureId}")
    public void renderPicture(@PathVariable("pictureId") String pictureId, HttpServletResponse response) {
        String path = campusProperties.getFileUploadPath() + pictureId;
        try {
            byte[] bytes = FileUtil.toByteArray(path);
            response.getOutputStream().write(bytes);
        } catch (Exception e) {
            //如果找不到图片就返回一个默认图片
            try {
                response.sendRedirect("/static/img/girl.gif");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
