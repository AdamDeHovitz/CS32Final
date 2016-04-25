<<<<<<< HEAD
bulkAppControllers.controller("accountCtrl", 
	function($scope, $http, $rootScope, $state, $ionicViewSwitcher) {

		console.log($rootScope.account);

		$scope.logOut = function() {
			$rootScope.account = {};
			$rootScope.authenticated = false;
			$ionicViewSwitcher.nextDirection('forward');
			$state.go('login');
		}
=======
bulkAppControllers.controller("accountCtrl", function($scope, $http, $rootScope) {

		console.log($rootScope.account);
>>>>>>> 58687b0c82a6c08a10ecaa94cc1fa591a3a5d9cf
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