bulkAppControllers.controller("settingsCtrl", function($scope, $rootScope,
    $http, $stateParams, $ionicPopup, $timeout, $state) {

	$scope.settingsData = {};
	$scope.forms = {};
	/*
	 * $scope.fullAccount = { firstName : 'Bob', lastName : 'Smith', email :
	 * 'l@l', img : 'link?' } $scope.settingsData.firstName =
	 * $scope.fullAccount.firstName; $scope.settingsData.lastName =
	 * $scope.fullAccount.lastName; $scope.settingsData.email =
	 * $scope.fullAccount.email;
	 */

	$.post("/account-info", {
		id : $scope.account.id
	}, function(responseJSON) {
		responseObject = JSON.parse(responseJSON);
		$timeout(function() {
			$scope.settingsData.firstName = responseObject.account.prof.firstName;
			$scope.settingsData.lastName = responseObject.account.prof.lastName;
			$scope.settingsData.email = responseObject.account.email;
		}, 0);
	});

	$scope.updateSettings = function() {
		console.log('Doing settings', $scope.settingsData);
		var myPopup = $ionicPopup.show({
		  template : '<input type="password" ng-model="settingsData.oldPassword">',
		  title : 'Confirm Changes',
		  subTitle : 'Please enter your current password to confirm changes',
		  scope : $scope,
		  buttons : [ {
			  text : 'Cancel'
		  }, {
		    text : '<b>Sumbit</b>',
		    type : 'button-positive',
		    onTap : function(e) {
			    if (!$scope.settingsData.oldPassword) {
				    // don't allow the user to close unless he enters wifi password
				    e.preventDefault();
			    } else {
				    console.log($scope.settingsData);
				    $scope.settingsData.id = $rootScope.account.id;
				    
				    $.post("/settings", $scope.settingsData, function(responseJSON) {
					    responseObject = JSON.parse(responseJSON);
					    console.log(responseObject);
					    
					    $timeout(function() {
						    if (!responseObject.hasError) {
							    $scope.settingsData = {};
						    	$rootScope.account.name = responseObject.firstName + " " + responseObject.lastName;
						    	$scope.settingsData.firstName = responseObject.firstName;
									$scope.settingsData.lastName = responseObject.lastName;
									$scope.settingsData.email = responseObject.email;
									$state.go("tab.account");
						    } else if (responseObject.failedAuth) {
						    	console.log("failedAuth");
						    	var alertPopup = $ionicPopup.alert({
						  		  title : "Failed Authentication",
						  		  template : "Try Again."
						  		});
						    }
						    
						    $scope.forms.settingsForm.$setPristine();
						    $scope.forms.settingsForm.$setUntouched();
					    }, 0)
				    });

			    }
		    }
		  } ]
		});
	}

	/*
	 * if (!$rootScope.userId) { $location.path("/"); } var postData = $.param({
	 * json: JSON.stringify({ userId: userId, }) }); $http.post("/feedEntries",
	 * postData).success(function(data, status) { $scope.events = data.entries;
	 * });
	 */
});