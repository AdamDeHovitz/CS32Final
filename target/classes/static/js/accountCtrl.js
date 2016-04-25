bulkAppControllers.controller("accountCtrl", 
	function($scope, $http, $rootScope, $state, $ionicViewSwitcher) {

		console.log($rootScope.account);

		$scope.logOut = function() {
			$rootScope.account = {};
			$rootScope.authenticated = false;
			$ionicViewSwitcher.nextDirection('forward');
			$state.go('login');
		}
		/*
		if (!$rootScope.userId) {
			$location.path("/");
		}
		var postData = $.param({
			json: JSON.stringify({
				userId: userId,
			})
		});
		$http.post("/feedEntries", postData).success(function(data, status) {
			$scope.events = data.entries;
		});*/
});