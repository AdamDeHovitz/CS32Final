bulkAppControllers.controller("pendingEventsCtrl", function($scope, $rootScope, $http) {
	$scope.curTitle = "Pending Events";
	$scope.curUrl = "events-event";
	$scope.curEvents = [];

	$.post("/event-pending", {id: $rootScope.account.id}, function(responseJSON) {
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