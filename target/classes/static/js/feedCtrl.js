bulkAppControllers.controller("feedCtrl", function($scope, $rootScope, $http) {
	$scope.curTitle = "Feed";
	$scope.curEvents = [];
	console.log("feed");
	$.post("/event-feed", {id: $rootScope.account.id}, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			$scope.curEvents = responseObject.events;
			console.log(responseObject);
	});

	

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