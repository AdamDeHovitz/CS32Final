bulkAppControllers.controller("loginCtrl", function($scope, $rootScope, $timeout, $state) {

	$scope.loginData = {};

	$scope.doLogin = function() {
    console.log('Doing login', $scope.loginData);

    	console.log('Doing login', $scope.loginData);

	    // Simulate a login delay. Remove this and replace with your login
	    // code if using a login system
	    $timeout(function() {
	    	$state.go("tab.feed");
	    }, 1000);
  	};
});
/*
bulkAppcontrollers.controller("loginCtrl", ['$scope', '$http', '$rootScope', '$location',
	function($scope, $http) {
		$scope.longin = function(username, password) {
			var postData = $.param({
				json: JSON.stringify({
					username: username,
					password: password
				})
			});
			$http.post("/login", postData).success(function(data, status) {
				$rootScope.userId = data.userID;
				$location.path("/home");
			});
		}

	}]);*/