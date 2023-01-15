// pages/crud/crud.js
const util = require("../../utils/util.js");
let app = getApp();
Page({

  /**
   * Page initial data
   */
  data: {

  },

  // insertRecord: function(e) {
  //   let newDate = new Date();
  //   console.log(newDate.format("yyyy-MM-dd h:m:s"));
  //   let sql = "insert into device_file(device_id, device_name, create_time) values('aaaa', 'bbbb', '" + newDate.format("yyyy-MM-dd h:m:s") + "')";
  //   wx.request({
  //     url: app.globalData.baseUrl + '/device_file_servlet_action',
  //     data: {
  //       "action": "update_record",
  //       "sql": sql
  //     },
  //     header: {
  //       "content-type":"application/x-www-form-urlencoded",
  //       "x-requested-with": "XMLHttpRequest"
  //     },
  //     success: function(res) {
  //       console.log(res.data);
  //     }
  //   })
  // },

  // deleteRecord: function() {
  //   let id = 960;
  //   let sql = "delete from `device_file` where id=" + id;
  //   wx.request({
  //     url: app.globalData.baseUrl + '/device_file_servlet_action',
  //     data: {
  //       'action': 'update_record',
  //       'sql': sql
  //     },
  //     header: {
  //       "content-type":"application/x-www-form-urlencoded",
  //       "x-requested-with": "XMLHttpRequest"
  //     },
  //     success: function(res) {
  //       console.log(res.data);
  //     }
  //   })
  // },
  
  // updateRecord: function() {
  //   let id = 961;
  //   let deviceName = '修改后';
  //   let sql = "update `device_file` set `device_name`='" + deviceName + "' where id=" + id;
  //   wx.request({
  //     url: app.globalData.baseUrl + '/device_file_servlet_action',
  //     data: {
  //       'action': 'update_record',
  //       'sql': sql
  //     },
  //     header: {
  //       "content-type":"application/x-www-form-urlencoded",
  //       "x-requested-with": "XMLHttpRequest"
  //     },
  //     success: function(res) {
  //       console.log(res.data);
  //     }
  //   })
  // },

  // selectRecord: function() {
  //   let sql = "select * from `device_file` order by `create_time`"
  //   wx.request({
  //     url: app.globalData.baseUrl + '/device_file_servlet_action',
  //     data: {
  //       'action': 'query_record',
  //       'sql': sql
  //     },
  //     header: {
  //       "content-type":"application/x-www-form-urlencoded",
  //       "x-requested-with": "XMLHttpRequest"
  //     },
  //     success: function(res) {
  //       console.log(res.data);
  //     }
  //   })
  // },

  goToInsertPage: function (e) {
    wx.navigateTo({
      url: '/pages/db/crud/insert',
    });
  },

  goToUpdatePage: function (e) {
    wx.navigateTo({
      url: '/pages/db/crud/update',
    });
  },

  goToSelectPage: function (e) {
    wx.navigateTo({
      url: '/pages/db/crud/select',
    });
  },

  /**
   * Lifecycle function--Called when page load
   */
  onLoad(options) {

  }
})