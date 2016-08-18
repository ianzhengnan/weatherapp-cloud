sap.ui.define([
	"sap/ui/core/mvc/Controller"
], function(Controller){
	"use strict";

	return Controller.extend("weather_app.view.Details", {

		onInit: function(){
			var oView = this.getView(),
				oRouter = this.getOwnerComponent().getRouter();

			oRouter.attachRouteMatched(function(oEvent){

				var mParameters = oEvent.getParameters();
				if (mParameters.view === oView) {
					var mArguments = oEvent.getParameter("arguments");
					var sId = mArguments.id;

					var aData = oView.getModel().getData();

					for (var i = 0; i < aData.length; i++) {
						if (aData[i].id === sId) {
							break;
						}
					}

					oView.bindElement("/" + i);

					var oAppModel = new sap.ui.model.json.JSONModel();

					jQuery.getJSON("api/v1/cities/" + sId + "/weather").done(function(mData){
						oAppModel.setData(mData);
						oView.setModel(oAppModel, "details");
					});
				}
			});

		},

		formatAddToFavVisible: function(sGuid){
			if (sGuid) {
				return false;
			}else{
				return true;
			}
		},

		formatRemoveFromFavVisible: function(sGuid){
			return !this.formatAddToFavVisible(sGuid);
		},

		handleAddToFavorite: function(oEvent){
			var oData = oEvent.getSource().getModel("details").getData();

			var that = this;

			$.ajax({
				url: "api/v1/cities/",
				method: "POST",
				contentType: "application/json",
				dataType: "json",
				data: JSON.stringify({
					"id": oData.id,
					"name": oData.name
				}),
				success: function(oResult, oResponse){
					that.getView().getModel().setData(oResult);
				}
			});
		},

		handleRemoveFromFavorite: function(oEvent){
			var oContext = oEvent.getSource().getBindingContext();
			var sId = oContext.getProperty().id;

			var that = this;
			$.ajax({
				url: "api/v1/cities/" + sId,
				method: "POST",
				success: function(oResult, oResponse){
					that.getView().getModel().setData(oResult);
				}
			});
		}
		
	});
});