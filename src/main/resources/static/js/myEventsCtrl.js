bulkAppControllers.controller("myEventsCtrl", function($scope, $rootScope, $http) {
	$scope.curTitle = "My Events";
	$scope.curEvents = [];

	$.post("/event-owner", {id: $rootScope.account.id}, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
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