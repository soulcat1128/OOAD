(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-2d0e8fe4"],{"8c47":function(e,r,s){"use strict";s.r(r);var o=function(){var e=this,r=e.$createElement,s=e._self._c||r;return s("div",{staticClass:"app-container"},[s("el-form",{ref:"form",attrs:{model:e.form,rules:e.passwordRules,"label-width":"100px"}},[s("el-form-item",{attrs:{label:"舊密碼",prop:"oldPassword"}},[s("el-input",{staticStyle:{width:"300px"},attrs:{type:"password",placeholder:"請輸入舊密碼"},model:{value:e.form.oldPassword,callback:function(r){e.$set(e.form,"oldPassword",r)},expression:"form.oldPassword"}})],1),s("el-form-item",{attrs:{label:"新密碼",prop:"newPassword"}},[s("el-input",{staticStyle:{width:"300px"},attrs:{type:"password",placeholder:"請輸入新密碼"},model:{value:e.form.newPassword,callback:function(r){e.$set(e.form,"newPassword",r)},expression:"form.newPassword"}})],1),s("el-form-item",{attrs:{label:"確認新密碼",prop:"repeat"}},[s("el-input",{staticStyle:{width:"300px"},attrs:{type:"password",placeholder:"請再輸入一遍新密碼"},model:{value:e.form.repeat,callback:function(r){e.$set(e.form,"repeat",r)},expression:"form.repeat"}})],1),s("el-form-item",[s("el-button",{attrs:{type:"primary"},on:{click:e.onSubmit}},[e._v("更改密碼")])],1)],1)],1)},t=[],a=s("5530"),l=(s("b0c0"),s("2f62")),n=s("c24f"),d={data:function(){var e=this,r=function(r,s,o){s!==e.form.newPassword?o(new Error("兩次輸入的密碼不一致!")):o()};return{form:{oldPassword:"",newPassword:"",repeat:""},passwordRules:{oldPassword:[{required:!0,message:"請輸入舊密碼",trigger:"blur"}],newPassword:[{required:!0,message:"請輸入新密碼",trigger:"blur"}],repeat:[{required:!0,message:"請再輸入新密碼",trigger:"blur"},{trigger:"blur",validator:r}]}}},methods:{onSubmit:function(){var e=this;this.$refs.form.validate((function(r){if(r){var s="admin"===e.roles[0]?1:0;console.log(s),Object(n["b"])({userid:e.id,username:e.name,isadmin:s,oldPassword:e.form.oldPassword,newPassword:e.form.newPassword}).then((function(r){0===r?e.$message.error("舊密碼不正確"):e.$message.success("修改成功")}))}else console.log("不允許提交!")}))}},computed:Object(a["a"])({},Object(l["b"])(["id","name","roles"]))},i=d,m=s("2877"),c=Object(m["a"])(i,o,t,!1,null,null,null);r["default"]=c.exports}}]);