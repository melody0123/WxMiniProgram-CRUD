// pages/crud/crud.js
const util = require("../../utils/util.js");
let app = getApp();
Page({

  /**
   * Page initial data
   */
  data: {

  },

  insertRecord: function(e) {
    let newDate = new Date();
    console.log(newDate.format("yyyy-MM-dd h:m:s"));
    let sql = "insert into device_file(device_id, device_name, create_time) values('aaaa', 'bbbb', '" + newDate.format("yyyy-MM-dd h:m:s") + "')";
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
  },

  deleteRecord: function() {

  },
  
  updateRecord: function() {

  },

  selectRecord: function() {

  },

  /**
   * Lifecycle function--Called when page load
   */
  onLoad(options) {

  }
})