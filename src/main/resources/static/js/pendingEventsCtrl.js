bulkAppControllers.controller("pendingEventsCtrl", function($scope, $rootScope, $http) {
	$scope.curTitle = "Pending Events";
	$scope.curUrl = "events-event";
	$scope.curEvents = [];
	$("#search").hide();

	$.post("/event-pending", {id: $rootScope.account.id}, function(responseJSON) {
			responseObject = JSON.parse(responseJSON);
			$scope.$apply(function() {
				$scope.curEvents = responseObject.events;
			});
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