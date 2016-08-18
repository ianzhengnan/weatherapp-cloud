sap.ui.define([
    "sap/ui/core/UIComponent",
    "sap/ui/model/odata/v2/ODataModel",
    "sap/ui/model/resource/ResourceModel"
    ], function(UIComponent, ODataModel, ResourceModel){
    "use strict";
    
    var ROUTING_TARGET_CONTROL_ID = "ROOT";

    var Component = UIComponent.extend("weather_app.Component",{

    	metadata: {
    		stereotype: "component",
    		version: "1.0",
    		rootView: "weather_app.view.App",
    		config: {
    			bundleUrl: "weather_app/i18n/i18n.properties"
    		},
    		routing: {
    			config: {
    				viewPath: "weather_app.view",
    				viewType: "XML"
    			},
    			routes: {
    				List: {
    					view: "List",
    					targetAggregation: "masterPages",
    					targetControl: ROUTING_TARGET_CONTROL_ID,
    					pattern: ":all*:",
    					subroutes: {
    						Details: {
    							view: "Details",
    							targetAggregation: "detailPages",
    							pattern: "weather/city/{id}"
    						}
    					}
    				}
    			}
    		}
    	},

    	init: function(){
    		UIComponent.prototype.init.apply(this, arguments);
    		this._initModels();

    		this.getRouter().initialize();
    	},

    	_initModels: function(){
    		var mConfig = this.getMetadata().getConfig();

    		var oAppModel = new sap.ui.model.json.JSONModel();

    		jQuery.getJSON("api/v1/cities").done(function(mData){
    			oAppModel.setData(mData);
    		});

    		this.setModel(oAppModel);

    		var oResourceModel = new ResourceModel({
    			bundleUrl: mConfig.bundleUrl
    		});

    		this.setModel(oResourceModel, "i18n");
    	}
    });

    return Component;
    
}, true /* export */);