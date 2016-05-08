bulkAppControllers.controller("settingsCtrl", function($scope, $rootScope,
    $http, $stateParams, $ionicPopup) {

	$scope.settingsData = {};
	$scope.forms = {};
	$scope.fullAccount = {
		  firstName : 'Bob',
		  lastName : 'Smith',
		  email : 'l@l',
		  img : 'link?'
		}
	$scope.settingsData.firstName = $scope.fullAccount.firstName;
	$scope.settingsData.lastName = $scope.fullAccount.lastName;
	$scope.settingsData.email = $scope.fullAccount.email;


	/*
	$.post("/account-info", {
		id : $scope.account.id
	}, function(responseJSON) {
		responseObject = JSON.parse(responseJSON);
		// $scope.reviews = responseObject.reviews;
		console.log(responseObject.reviews);
	});*/



	$scope.updateSettings = function() {
		console.log('Doing settings', $scope.settingsData);
		var myPopup = $ionicPopup.show({
		  template : '<input type="password" ng-model="data.password">',
		  title : 'Confirm Changes',
		  subTitle : 'Please enter your current password to confirm changes',
		  scope : $scope,
		  buttons : [ {
			  text : 'Cancel'
		  }, {
		    text : '<b>Sumbit</b>',
		    type : 'button-positive',
		    onTap : function(e) {
			    if (!$scope.settingsData.password) {
				    // don't allow the user to close unless he enters wifi password
				    e.preventDefault();
			    } else {
				    console.log($scope.settingsData);
				    /*
						 * $.post("/account-settings", settingsData, function(responseJSON) {
						 * responseObject = JSON.parse(responseJSON); $scope.registerData =
						 * {}; $scope.registerData['firstName'] = responseObject.firstName;
						 * $scope.registerData['lastName'] = responseObject.lastName;
						 * $scope.registerData['email'] = responseObject.email;
						 * $scope.forms.settingsForm.$setPristine();
						 * $scope.forms.settingsForm.$setUntouched(); });
						 */
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