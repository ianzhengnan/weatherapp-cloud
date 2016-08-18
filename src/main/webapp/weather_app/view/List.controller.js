sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"sap/ui/model/Sorter"
], function(Controller, Sorter){
	"use strict";

	return Controller.extend("weather_app.view.List", {

		_showWeatherInfoForListItem: function(oListItem){

			var oRouter = this.getOwnerComponent().getRouter(),
				oContext = oListItem.getBindingContext(), 
				sPath = oContext ? oContext.getPath() : "0";

			oRouter.navTo("Details", {
				id: oContext.getProperty().id,
				path: encodeURIComponent(sPath)
			});
		},

		handleHomeButtonPress: function(){
			var oRouter = this.getOwnerComponent().getRouter();
			oRouter.navTo("Launchpad");
		},

		handleListItemPress: function(oEvent){
			var oListItem = oEvent.getParameter("listItem");
			this._showWeatherInfoForListItem(oListItem);
		},

		handleSearch: function(oEvent){

			var sTerm = oEvent.getParameter("query");

			var oAppModel = this.getView().getModel();

			jQuery.getJSON("api/v1/weather?q=" + sTerm).done(function(mData){
				oAppModel.setData([
					{
						id: mData.id,
						name: mData.name
					}
				]);
			});
		}
	});


});