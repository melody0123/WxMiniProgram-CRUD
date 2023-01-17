const util = require("../../../utils/util.js");
let app = getApp();
Page({
  data: {
    deviceId: "",
    deviceName: ""
  },

  onLoad: function () {
  },

  getDeviceName: function (e) {
    this.setData({
      deviceName: e.detail.value
    });
  },

  getDeviceId: function (e) {
    this.setData({
      deviceId: e.detail.value
    });
  },

  applySubmit:function(){
    this.addDBRecord();
  },

  addDBRecord: function (e) {
    let newDate = new Date();
    console.log(newDate.format("yyyy-MM-dd h:m:s"));
    let sql = "insert into device_file(device_id, device_name, create_time) values('" + this.data.deviceId + "', '" + this.data.deviceName + "', '" + newDate.format("yyyy-MM-dd h:m:s") + "')";
    wx.request({
      url: app.globalData.baseUrl + '/device_file_servlet_action',
      data: {
        "action": "update_record",
        "sql": sql
      },
      header: {
        "content-type":"application/x-www-form-urlencoded",
        "x-requested-with": "XMLHttpRequest"
      },
      success: function(res) {
        console.log(res.data);
      }
    })
  }
})
