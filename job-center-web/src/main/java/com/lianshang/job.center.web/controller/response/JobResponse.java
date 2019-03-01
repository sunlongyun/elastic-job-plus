package com.lianshang.job.center.web.controller.response;

import java.io.Serializable;
import lombok.Data;

/**
 * 返回值
 *
 * @author 孙龙云
 */
@Data
public class JobResponse implements Serializable {

  private static final long serialVersionUID = -7096601129585145021L;
  private String code = null;
  private String msg = null;
  private Object data = null;

  private JobResponse(String code, String msg, Object data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }

  public static JobResponse success() {
    return success(JobResponseCodeEnum.SUCCESS.msg());
  }

  public static JobResponse success(String msg) {
    JobResponse res = new JobResponse(JobResponseCodeEnum.SUCCESS.code(), msg, null);
    return res;
  }


  public static JobResponse success(Object data) {
    JobResponse res = new JobResponse(JobResponseCodeEnum.SUCCESS.code(), JobResponseCodeEnum.SUCCESS.msg(), data);
    return res;
  }


  public static JobResponse fail(String msg) {
    JobResponse res = new JobResponse(JobResponseCodeEnum.FAIL.code(), msg, null);
    return res;
  }

  public JobResponse() {

  }
}
